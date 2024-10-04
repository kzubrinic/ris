package hr.unidu.ris.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateServer {
	public static void main(String[] args) {
		System.out.println("Poslužitelj je pokrenut i čeka klijente.");
		boolean radi = true;
		try (ServerSocket listener = new ServerSocket(9090)) {
			while (radi) {
				try (Socket clientSocket = listener.accept()) {
					System.out.println("Klijent " + clientSocket.getInetAddress() + " se povezao na poslužitelj.");
					// Obrađivanje klijentskog zahtjeva
					InputStream input = clientSocket.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(input));
					PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
					// Čekanje na klijentove podatke
					String param = reader.readLine();
					System.out.println("Primljen zahtjev: " + param);
					// Priprema i slanje odgovora na klijentov zahtjev
					String odgovor = "Odgovor na zahtjev: " + param + ": ";
					if ("DATUM".equals(param)) {
						odgovor = odgovor + new SimpleDateFormat("dd.MM.yyyy").format(new Date());
					} else if ("VRIJEME".equals(param)) {
						odgovor = odgovor + new SimpleDateFormat("HH:mm:ss").format(new Date());
					} else if ("KRAJ".equals(param)) {
						odgovor = odgovor + "Klijent zaustavlja poslužitelj.";
						radi = false;
					}
					writer.println(odgovor);
				} catch (IOException e) {
					System.out.println("Greška pri komunikaciji s klijentom: " + e.getMessage());
				}
			}
		} catch (IOException e1) {
			System.out.println("Greška u radu poslužitelja: " + e1.getMessage());
		}
	}
}