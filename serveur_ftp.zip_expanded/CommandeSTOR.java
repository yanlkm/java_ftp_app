import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class CommandeSTOR extends Commande {

	public CommandeSTOR(PrintStream ps, String commandeStr, CommandExecutor comex) {
		super(ps, commandeStr, comex);
	}

	public void execute() {

		try {
			// Ouvrir un flux de sortie sur le fichier
			System.out.println("Exécution de la commande STOR");

			File file = new File(comex.repertoireCourant + "/" + commandeArgs[0]);

			System.out.println("Receiving file: " + file.getName());

			FileOutputStream fos = new FileOutputStream(file);

			// Lire le contenu du fichier envoyÃ© par le client

			Socket socket = new Socket("localhost", 4000);
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
			

			byte[] buffer = new byte[10000];
			int bytesRead = 0;
			while ((bytesRead = bis.read(buffer)) > 0) {

				fos.write(buffer, 0, bytesRead);
				

			}
			bis.close();

			// Fermer le flux de sortie
			fos.close();
			socket.close();
			// Afficher un message de confirmation
			System.out.println(" Le fichier a été sauvegardé : " + file.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
