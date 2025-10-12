# jednostavan server koji očekuje konekcije klijenata
import socket
import threading
from datetime import datetime

HOST = "127.0.0.1"
PORT = 9090

clients_by_id = {}  # clientId -> (socket, thread)
clients_lock = threading.Lock()

def handle_client(conn, addr):
    try:
        conn_file = conn.makefile("r", encoding="utf-8")
        first = conn_file.readline()
        if not first or not first.startswith("ID:"):
            conn.sendall("GREŠKA: Prva naredba mora biti ID:neki_id\n".encode("utf-8"))
            print(f"Klijent {addr} nije poslao ID kao prvu naredbu.")
            conn.close()
            return

        client_id = first[3:].strip()
        if not client_id:
            conn.sendall("GREŠKA: ID prazan\n".encode("utf-8"))
            conn.close()
            return

        # Provjera da li je client_id već spojen
        with clients_lock:
            if client_id in clients_by_id:
                conn.sendall("GREŠKA: Taj klijent je već prijavljen. Veza odbijena.\n".encode("utf-8"))
                print(f"Odbijena veza s {addr} - clientId '{client_id}' već aktivan.")
                conn.close()
                return
            clients_by_id[client_id] = conn

        conn.sendall("ID_OK\n".encode("utf-8"))
        print(f"Klijent '{client_id}' povezan s {addr}")

        # glavna petlja za naredbe
        while True:
            line = conn_file.readline()
            if not line:
                break
            cmd = line.strip()
            if not cmd:
                continue
            if cmd.startswith("ID:"):
                conn.sendall("GREŠKA: ID se ne može mijenjati nakon spajanja.\n".encode("utf-8"))
                print(f"Klijent '{client_id}' pokušao promijeniti ID.")
                continue

            cmd_upper = cmd.upper()
            if cmd_upper == "DATUM":
                datum = datetime.now().strftime("%d.%m.%Y")
                conn.sendall((datum + "\n").encode("utf-8"))
            elif cmd_upper == "VRIJEME":
                vrijeme = datetime.now().strftime("%H:%M:%S")
                conn.sendall((vrijeme + "\n").encode("utf-8"))
            elif cmd_upper == "KRAJ":
                conn.sendall("Zatvaram vezu. Doviđenja!\n".encode("utf-8"))
                break
            else:
                conn.sendall(f"Nepoznata naredba: {cmd}\n".encode("utf-8"))
    except KeyboardInterrupt:
        print("\nCtrl+C detektiran, gasim server...")
    except Exception as e:
        print(f"Greška s klijentom {addr}: {e}")
    finally:
        with clients_lock:
            clients_by_id.pop(client_id, None)
        conn.close()
        print(f"Veza s clientId='{client_id}' zatvorena.")


def main():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((HOST, PORT))
        s.listen()
        print(f"Server pokrenut na {HOST}:{PORT}")

        try:
            while True:
                conn, addr = s.accept()
                threading.Thread(target=handle_client, args=(conn, addr), daemon=True).start()
        except KeyboardInterrupt:
            print("Server se zaustavlja...")
        finally:
            with clients_lock:
                for conn in clients_by_id.values():
                    try: conn.close()
                    except: pass

if __name__ == "__main__":
    main()
