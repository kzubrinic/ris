package hr.unidu.ris.clientserver;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DateServer - jednostavan server koji očekuje konekcije klijenata
 * - Prva poruka od svakog klijenta mora biti "ID:neki_id"
 * - clientId se pohranjuje po vrijednosti ID-ja
 * - Veza ostaje otvorena dok klijent ne pošalje "KRAJ"
 * - Drugi pokušaj spajanja s istim clientId se odbija (prevencija preuzimanja sesije)
 */
public class DateServer {
    private static final int PORT = 9090;
    private static final int THREAD_POOL = 20;

    private static final ConcurrentHashMap<String, ClientHandler> clientsById = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL);
        System.out.println("Poslužitelj pokrenut na portu " + PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown: gašenje bazena dretvi i klijenata...");
            pool.shutdownNow();
            clientsById.forEach((id, handler) -> handler.disconnect());
        }));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            while (!pool.isShutdown()) {
                try {
                    Socket sock = serverSocket.accept();
                    sock.setSoTimeout(0);
                    // obrada novog konekta u thread poolu
                    pool.submit(() -> {
                        try {
                            handleNewConnection(sock);
                        } catch (IOException e) {
                            System.err.println("Greška pri obradi novog klijenta: " + e.getMessage());
                            try { sock.close(); } catch (IOException ignored) {}
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Accept: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server socket error: " + e.getMessage());
        } finally {
            pool.shutdown();
            System.out.println("Server zaustavljen.");
        }
    }

    /**
     * Prihvatimo naredbu i inicijaliziramo ClientHandler.
     * Prva naredba mora biti oblika "ID:neki_id". Ako nije, vratimo grešku i zatvorimo vezu.
     */
    private static void handleNewConnection(Socket sock) throws IOException {
        SocketAddress remote = sock.getRemoteSocketAddress();
        try (
            BufferedReader r = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
            PrintWriter w = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"), true)
        ) {
            String first = r.readLine();

            if (first == null|| !first.startsWith("ID:")) {
                w.println("GREŠKA: Prva naredba mora biti ID:neki_id");
                System.out.println("Klijent " + remote + " nije poslao ID kao prvu naredbu.");
                sock.close();
                return;
            }

            String clientId = first.substring(3).trim();
            if (clientId.isEmpty()) {
                w.println("GREŠKA: ID prazan");
                sock.close();
                return;
            }

            // Provjeri je li taj ID već prijavljen
            ClientHandler existing = clientsById.putIfAbsent(clientId, new ClientHandler(clientId, sock, r, w));
            if (existing != null) {
                // postoji već aktivna veza sa tim ID-om -> odbijamo novi konekt
                w.println("GREŠKA: Taj klijent je već prijavljen. Veza odbijena.");
                System.out.println("Odbijena veza s " + remote + " - clientId '" + clientId + "' već aktivan.");
                sock.close();
                return;
            }

            // Preuzimamo handler iz mape (stvaramo novu instancu u putIfAbsent)
            ClientHandler handler = clientsById.get(clientId);
            System.out.println("Klijent '" + clientId + "' povezan s " + remote);
            w.println("ID_OK");

            // Pokreni handling (u svojoj dretvi) - ovdje handler već koristi isti reader/writer i socket
            handler.runHandling();

            // kada se handler završi (npr. poslije KRAJ), ukloni ga iz mape
            clientsById.remove(clientId);
            System.out.println("Klijent '" + clientId + "' odjavljen.");
        } catch (IOException e) {
            try { sock.close(); } catch (IOException ignored) {}
            throw e;
        }
    }

    // Inner klasa koja drži state po clientId
    private static class ClientHandler {
        private final String clientId;
        private final Socket socket;
        private final BufferedReader reader;
        private final PrintWriter writer;
        private volatile boolean active = true;

        // Konstruktor koji sprema postojeće streamove (korišten samo unutar handleNewConnection)
        ClientHandler(String clientId, Socket socket, BufferedReader reader, PrintWriter writer) {
            this.clientId = clientId;
            this.socket = socket;
            this.reader = reader;
            this.writer = writer;
        }

        // Pokreni glavnu petlju za ovaj klijent (poziva se u thread pool tasku)
        void runHandling() {
            try {
                String line;
                while (active && (line = reader.readLine()) != null) {
                    if (line.isBlank()) continue;

                    // Ako pokuša promijeniti ID nakon prijave -> odbij, ali ne prekidaj vezu
                    if (line.startsWith("ID:")) {
                        writer.println("GREŠKA: ID se ne može mijenjati nakon spajanja.");
                        System.out.println("Klijent '" + clientId + "' je pokušao promijeniti ID.");
                        break;
                    }

                    // Procesuiraj naredbu
                    boolean shouldClose = processCommand(line);
                    if (shouldClose) break; // KRAJ => izađi i zatvori
                }
            } catch (IOException e) {
                System.out.println("IO error s clientId=" + clientId + ": " + e.getMessage());
            } finally {
                disconnect();
            }
        }

        // Vrati true ako treba zatvoriti vezu
        private boolean processCommand(String raw) {
            String cmd = raw.trim();
            String cmdUpper = cmd.toUpperCase();
            System.out.println("[" + clientId + "] poslao: " + cmd);

            switch (cmdUpper) {
                case "DATUM":
                    String datum = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
                    writer.println(datum);
                    return false;
                case "VRIJEME":
                    String vrijeme = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    writer.println(vrijeme);
                    return false;
                case "KRAJ":
                    writer.println("Zatvaram vezu. Doviđenja!");
                    active = false;
                    return true;
                default:
                    writer.println("Nepoznata naredba: " + cmd);
                    return false;
            }
        }

        // Sigurno zatvori socket i strimove
        void disconnect() {
            active = false;
            try { socket.close(); } catch (IOException ignored) {}
            System.out.println("Veza s clientId='" + clientId + "' zatvorena.");
        }
    }
}
