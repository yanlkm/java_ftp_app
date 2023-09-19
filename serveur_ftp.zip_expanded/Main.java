
/*
 * TP JAVA RIP
 * Min Serveur FTP
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	static ServerSocket serveur = null;

	public static void main(String[] args) throws Exception {

		Thread serverThread = new Thread(() -> {
			try {
				int port = 8923;
				try {
					serveur = new ServerSocket(port);
					System.out.println("Le Serveur FTP");
				} catch (IOException e) {

					System.out.println("Erreur de création du serveur FTP !");
					return;
				}

				while (true) {
					try {
						Socket sock_serveur = serveur.accept();
						System.out.println("Connexion établie avec le client sur l'AddresseIP : "
								+ sock_serveur.getInetAddress().getHostAddress());

						Thread clientThread = new Thread(() -> {
							try {
								CommandExecutor exec = new CommandExecutor();

								if (sock_serveur.isClosed()) {
									exec.userOk = false;
									exec.pwOk = false;
								}

								BufferedReader recevoir = new BufferedReader(
										new InputStreamReader(sock_serveur.getInputStream()));
								PrintStream envoi = new PrintStream(sock_serveur.getOutputStream());
								envoi.println("1 Bienvenue ! ");
								String commande = "";
								while (true) {
									if (recevoir.ready()) {
										commande = recevoir.readLine();
										if (commande.equals("bye")) {
											break;
										}
										exec.executeCommande(envoi, commande);
									}
								}
								exec.userOk = false;
								exec.pwOk = false;
								envoi.close();
								recevoir.close();
								sock_serveur.close();
								System.out.println("Client déconnecté !");
								if (sock_serveur.isClosed()) {
									recevoir.close();
									envoi.close();
								}
							} catch (IOException ex) {
								System.out.println("Erreur dans l'exécution du thread!");
							}
						});
						clientThread.start();
					} catch (Exception e) {
						System.out.println("Erreur dans la connexion au socket");
					}
				}
			} catch (Exception ex) {
				System.out.println("Erreur dans la création du thread!");
			}
		});
		serverThread.start();

	}
}
