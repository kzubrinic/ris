# jednostavan socket klijent
import socket
import sys

# Provjeri argumente komandne linije
if len(sys.argv) >= 3:
    HOST = sys.argv[1]          # prvi argument = adresa servera
    PORT = int(sys.argv[2])     # drugi argument = port servera
else:
    HOST = "127.0.0.1"
    PORT = 9090

cid = input("Unesi ID klijenta: ").strip()

# Otvori vezu samo jednom
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    # Pošalji ID odmah nakon spajanja
    s.sendall(f"ID:{cid}\n".encode("utf-8"))
    data = s.recv(1024)
    print("Server:", data.decode("utf-8").strip())

    while True:
        cmd = input("Unesi naredbu (DATUM, VRIJEME, KRAJ): ").strip()
        if not cmd:
            continue
        # Pošalji naredbu kroz istu vezu
        s.sendall((cmd + "\n").encode("utf-8"))

        # primi odgovor servera
        try:
            data = s.recv(1024)
            if not data:
                print("Server je zatvorio vezu nakon ID-a.")
                exit()
        except ConnectionAbortedError:
            print("Veza je prekinuta od strane servera nakon slanja ID-a.")
            exit()
            
        odgovor = data.decode("utf-8").strip()
        print("Odgovor:", odgovor)

        if cmd.upper() == "KRAJ":
            break
