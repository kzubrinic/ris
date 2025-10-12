/*
 * Jednostavak klijent koji se spaja na poslužitelj
 */
package hr.unidu.ris.clientserver;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DateClient {

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9090;
        
        if (args.length >= 2) {
        	host = args[0];
        	port = Integer.parseInt(args[1]);
        }

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
             Scanner sc = new Scanner(System.in)) {

            // Pošalji ID
            System.out.print("Unesi svoj ID: ");
            String clientId = sc.nextLine().trim();
            out.println("ID:" + clientId);

            // Prihvati odgovor servera
            String response = in.readLine();
            if (response == null) {
                System.out.println("Server je zatvorio vezu.");
                return;
            }
            System.out.println("Server: " + response);

            // Ako server odbije (ID_OK nije vraćen) -> prekini
            if (!response.equals("ID_OK")) {
                System.out.println("Neuspješna prijava ID-ja. Zatvaram klijent.");
                return;
            }

            // Glavna petlja za slanje naredbi
            while (true) {
                System.out.print("Unesi naredbu (DATUM, VRIJEME, KRAJ): ");
                String command = sc.nextLine().trim();

                if (command.isEmpty()) continue;

                out.println(command);

                // primi odgovor
                response = in.readLine();
                if (response == null) {
                    System.out.println("Server je zatvorio vezu.");
                    break;
                }
                System.out.println("Odgovor: " + response);

                // prekid ako je KRAJ
                if (command.equalsIgnoreCase("KRAJ")) {
                    System.out.println("Zatvaram vezu prema serveru...");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Greška u vezi sa serverom: " + e.getMessage());
        }
    }
}
