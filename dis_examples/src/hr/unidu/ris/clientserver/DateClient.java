package hr.unidu.ris.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DateClient {
    /**
     * Pokreće klijent u kojem trebate upisati IP adresu/naziv poslužitelja na kojem je na portu 
     * 9090 pokrenut poslužitelj. Nakon toga se povezuje s poslužiteljem, šalje mu parametar pa
     * ovisno o parametru dohvaća trenutni datum ili vrijeme i prikazuje ga na zaslonu.
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Adresa poslužitelja: ");
        String adresaPosluzitelja = sc.nextLine();
        while(true) {
	        System.out.print("Parametar (DATUM/VRIJEME/KRAJ): ");
	        String par = sc.nextLine();
	        Socket s = new Socket(adresaPosluzitelja, 9090);
	        PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
	        writer.println(par);
	        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        System.out.println(input.readLine());
	        input.close();
	        s.close();
	        if ("KRAJ".equals(par)) {
	        	break;
	        }
        }
        sc.close();
    }
}
