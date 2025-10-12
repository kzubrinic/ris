/*
Jednostavan C klijent koji se spaja na socket server koji vraca trenutni datum ili vrijeme
*/
#include <stdio.h>
#include <string.h>
#include <winsock2.h>
#include <ws2tcpip.h>

#pragma comment(lib, "ws2_32.lib")

void uppercase(char *str) {
    while (*str) {
        if (*str >= 'a' && *str <= 'z') {  
            *str = (char)(*str - 'a' + 'A');
        }
        str++;
    }
}

int main(int argc, char* argv[]) {
    WSADATA wsa;
    SOCKET sock;
    struct sockaddr_in server;
    char id[100], command[100], buffer[1024];
	char server_ip[16];
    int port = 9090;
	if (argc >= 3){
		strcpy(server_ip, argv[1]);
		sscanf(argv[2], "%d", &port);
	} else {
		strcpy(server_ip, "127.0.0.1");
		port = 9090;
	}

    WSAStartup(MAKEWORD(2,2), &wsa);

    // Učitaj ID
    printf("Unesi ID korisnika: ");
    fgets(id, sizeof(id), stdin);
    id[strcspn(id, "\r\n")] = 0;  // ukloni newline

    // Kreiraj socket
    sock = socket(AF_INET, SOCK_STREAM, 0);

    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    inet_pton(AF_INET, server_ip, &server.sin_addr);

    connect(sock, (struct sockaddr*)&server, sizeof(server));

    // Pošalji ID
    sprintf(buffer, "ID:%s\n", id);
    send(sock, buffer, strlen(buffer), 0);

    // Prihvati prvi odgovor
    int n = recv(sock, buffer, sizeof(buffer)-1, 0);
    if (n > 0) {
        buffer[n] = '\0';
        printf("Server: %s\n", buffer);
    }

    // Glavna petlja naredbi
    while (1) {
        printf("Unesi naredbu (DATUM, VRIJEME, KRAJ): ");
        fgets(command, sizeof(command), stdin);
        command[strcspn(command, "\r\n")] = 0;  // ukloni newline

        sprintf(buffer, "%s\n", command);
        send(sock, buffer, strlen(buffer), 0);

        n = recv(sock, buffer, sizeof(buffer)-1, 0);
        if (n > 0) {
            buffer[n] = '\0';
            printf("Odgovor: %s\n", buffer);
        }
		uppercase(command);
        if (strcmp(command, "KRAJ") == 0)
            break;
    }

    closesocket(sock);
    WSACleanup();
    return 0;
}