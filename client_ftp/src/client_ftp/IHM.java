import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IHM extends Application {

	private Socket socket;
	private BufferedReader recevoir;
	private PrintStream envoi;
	static int flag = 0;

	@Override
	public void start(Stage primaryStage) {

		// Créer une zone de texte pour afficher les réponses du serveur
		Font fontTitle = Font.font("Verdana", FontWeight.BOLD, 17);

		Font font = Font.font("Arial", FontWeight.BOLD, 15);

		TextArea responseArea = new TextArea();

		responseArea.setFont(font);

		responseArea.setEditable(false);
		primaryStage.setTitle("Client FTP");

		BorderPane layout = new BorderPane();

		Button connectButton = new Button("Se connecter");
		Button disconnectButton = new Button("Déconnecter");
		Button clearButton = new Button("Nettoyer");

		disconnectButton.setDisable(true); // Inactiver le bouton "Déconnecter" au début
		clearButton.setDisable(true); // Inactiver le bouton "Nettoyer" au début

		connectButton.setOnAction(e -> {

			try {
				socket = new Socket("localhost", 8923);
				recevoir = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				envoi = new PrintStream(socket.getOutputStream());
				String reponse = recevoir.readLine();
				if (reponse.startsWith("1 Bienvenue ! ")) {
					responseArea.appendText("\n");
					responseArea.appendText("\nConnexion réussie !\n");
					connectButton.setDisable(true); // Masquer le bouton "Se Connecter"
					disconnectButton.setDisable(false); // Afficher le bouton "Déconnecter"
					clearButton.setDisable(false); // Afficher le bouton "Nettoyer"
				} else {
					responseArea.appendText("\n2 Erreur de connexion : " + reponse + "\n");
				}
			} catch (IOException ex) {
				responseArea.appendText("\n");
				responseArea.appendText("\n2 Erreur de connexion : " + ex.getMessage() + "\n");
			}
		});

		disconnectButton.setOnAction(e -> {

			try {
				flag = 0;
				envoi.println("bye");
				responseArea.appendText("\n");
				String reponse = recevoir.readLine();
				responseArea.appendText("\n\nVous êtes déconnecté");
				envoi.close();
				recevoir.close();
				socket.close();
				clearButton.setDisable(true); // Masquer le bouton "Nettoyer"
				disconnectButton.setDisable(true); // Masquer le bouton "Déconnecter"
				connectButton.setDisable(false); // Afficher le bouton "Se Connecter"

			} catch (IOException ex) {
				responseArea.appendText("\n");
				responseArea.appendText("\n2 Erreur lors de la fermeture de la connexion : " + ex.getMessage() + "\n");
			}
		});
		clearButton.setOnAction(e -> {

			try {
				responseArea.clear();

			} catch (Exception ex) {
				responseArea.appendText("\n");
				responseArea.appendText("\n2 Erreur lors du nettoyage " + ex.getMessage() + "\n");
			}
		});

		// Mettre les deux boutons dans la même boîte
		HBox inputBox = new HBox(connectButton, disconnectButton, clearButton);
		inputBox.setAlignment(Pos.CENTER);
		inputBox.setSpacing(10);
		inputBox.setPadding(new Insets(40));
		// Créer une zone de texte
		Label responseLabel = new Label("Réponse du serveur");
		responseArea.setWrapText(true);
		responseLabel.setFont(fontTitle);

		// Créer une barre de défilement pour la zone de texte
		ScrollPane scroll = new ScrollPane(responseArea);
		scroll.setFitToWidth(true);
		scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

		// Ajouter l'étiquette à un conteneur
		VBox responseBox = new VBox();
		responseBox.getChildren().addAll(responseLabel, scroll);
		responseBox.setSpacing(10);
		responseBox.setPadding(new Insets(10));

		// Ajouter le conteneur au panneau principal
		BorderPane responsePane = new BorderPane();
		responsePane.setCenter(responseBox);

		// Ajouter les éléments à la mise en page
		layout.setTop(inputBox);
		layout.setCenter(responsePane);

		// Créer un champ de texte pour les commandes FTP
		TextField commandField = new TextField();
		commandField.setPromptText("Commande FTP");
		commandField.setPrefSize(300, 50);

		// Créer un bouton pour envoyer la commande FTP

		Button sendButton = new Button("Envoyer");
		sendButton.setPrefSize(100, 25);
		sendButton.setOnAction(e -> {

			String commande = commandField.getText();
			String ligne = commande;
			commandField.setText("");

			responseArea.appendText("\n");
			if (socket != null && !socket.isClosed()) {
				try {
					// Si le user est deja connecté il ne pourra plus se reconnecter

					if ((commande.startsWith("pass") || (commande.startsWith("user"))) && flag == 2) {
						responseArea.appendText("\n\nVous êtes déjà connectés");
						return;

					}

					if (((commande.startsWith("user"))) && flag == 0) {
						envoi.println(commande);
						String reponse = recevoir.readLine();
						responseArea.appendText("\n");
						responseArea.appendText(reponse);
						if (reponse.startsWith("0")) {
							flag = 1;
						}
						return;

					}
					if (((commande.startsWith("pass"))) && flag == 1) {
						envoi.println(commande);
						String reponse = recevoir.readLine();
						responseArea.appendText("\n");
						responseArea.appendText(reponse);
						if (reponse.startsWith("0")) {
							flag = 2;
						}

						return;

					}
					// commandes envoyées au serveur
					if (commande.startsWith("stor")) {
						if ((commande.split(" ").length < 2) || ((commande.split(" ").length > 2))) {
							responseArea.appendText("\nVeuillez entrer le bon nombre d'argument pour la commande ");
							return;
						}

						Thread storThread = new Thread(() -> {
							try {
								envoi.println(ligne);
								ServerSocket sock = new ServerSocket(4000);
								Socket socket_stor = sock.accept();
								String[] arguments = ligne.split(" ");
								BufferedOutputStream dataOut = new BufferedOutputStream(socket_stor.getOutputStream());
								File file = new File(arguments[1]);

								if (file.exists()) {
									responseArea.appendText(
											"\n"+file.getAbsolutePath());
									FileInputStream fis = new FileInputStream(file);

									byte[] buffer = new byte[1024];
									int bytesRead = 0;
									while ((bytesRead = fis.read(buffer)) != -1) {
										dataOut.write(buffer, 0, bytesRead);
										dataOut.flush();
									}
									fis.close();
									dataOut.close();
									socket_stor.close();
									sock.close();
								} else {
									dataOut.close();
									socket_stor.close();
									sock.close();
									responseArea.appendText(
											"\n2 le fichier que vous essayez de téléverser n'existe pas ou est mal placé ");
									return;
								}
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						});
						storThread.start();
					} else if (commande.startsWith("get")) {
						if ((commande.split(" ").length < 2) || ((commande.split(" ").length > 2))) {
							responseArea.appendText("\n2 Veuillez entrer le bon nombre d'argument pour la commande ");

							return;

						}

						Thread getThread = new Thread(() -> {

							try {

								envoi.println(ligne);
								ServerSocket sock = new ServerSocket(4041);
								Socket socket_get = sock.accept();

								String[] arguments = ligne.split(" ");
								System.out.println(arguments[1]);
								File file = new File(System.getProperty("user.dir") + "/" + arguments[1]);
								BufferedInputStream bis = new BufferedInputStream(
										socket_get.getInputStream());
								FileOutputStream fos = new FileOutputStream(file);

								
								
								byte[] buffer = new byte[10000];
								int bytesRead = 0;
								while ((bytesRead = bis.read(buffer)) > 0) {

									fos.write(buffer, 0, bytesRead);
									

								}
								bis.close();
								fos.close();
								socket_get.close();
								sock.close();
								responseArea.appendText("\n0 Le fichier a été sauvegardé : " + file.getAbsolutePath());
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						});
						getThread.start();
					} else if (commande.equals("")) {
						responseArea.appendText("\nVeuillez entrer UNE commande spécifique");
						return;

					} else if (!(commande.startsWith("rmdir") || commande.startsWith("user")
							|| commande.startsWith("pass") || commande.startsWith("ls") || commande.startsWith("mkdir")
							|| commande.startsWith("rmdir") || commande.startsWith("adduser")
							|| commande.startsWith("pwd") || commande.startsWith("cd"))) {
						responseArea.appendText("\nVeuillez entrer UNE commande spécifique");
						return;

					} else if (((commande.startsWith("user") || commande.startsWith("pass")
							|| commande.startsWith("mkdir") || commande.startsWith("rmdir"))
							&& (commande.split(" ").length < 2))
							|| ((commande.startsWith("user") || commande.startsWith("pass")
									|| commande.startsWith("mkdir") || commande.startsWith("rmdir"))
									&& (commande.split(" ").length > 2))) {

						responseArea.appendText("\nVeuillez entrer le bon nombre d'argument pour la commande ");
						return;

					} else if (((commande.startsWith("adduser")) && (commande.split(" ").length < 3))
							|| ((commande.startsWith("adduser")) && (commande.split(" ").length > 3))) {
						responseArea.appendText("\nVeuillez entrer le bon nombre d'argument pour la commande ");
						return;
					} else {
						envoi.println(ligne);
						String reponse = recevoir.readLine();
						responseArea.appendText("\n");
						responseArea.appendText(reponse);
					}

				} catch (IOException ex) {
					responseArea.appendText("\n");
					responseArea.appendText("\n2 Erreur lors de l'envoi de la commande: " + ex.getMessage() + "\n");
				}
			} else {
				responseArea.appendText("\n");
				responseArea.appendText("\nLa connexion n'est pas établie\n");
			}

		});
		// Créer une boîte horizontale pour le champ de texte et le bouton d'envoi
		HBox commandBox = new HBox(commandField, sendButton);
		commandBox.setAlignment(Pos.CENTER);
		commandBox.setSpacing(10);
		commandBox.setPadding(new Insets(10));

		// Ajouter la boîte de commande à la mise en page
		layout.setBottom(commandBox);

		// Créer la scène et l'afficher
		Scene scene = new Scene(layout, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
