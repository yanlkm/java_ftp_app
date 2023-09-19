import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.Socket;

public class CommandeGET extends Commande {

	public CommandeGET(PrintStream ps, String commandeStr, CommandExecutor comex) {
		super(ps, commandeStr, comex);
	}

	public void execute() {

		System.out.println("Exécution de la commande GET");

		try {
			Socket socket_get = new Socket("localhost", 4041);

			BufferedOutputStream dataOut = new BufferedOutputStream(socket_get.getOutputStream());

			File file = new File(comex.repertoireCourant + "/" + commandeArgs[0]);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);

				byte[] buffer = new byte[10000];
				int bytesRead = 0;
				while ((bytesRead = fis.read(buffer)) > 0) {

					dataOut.write(buffer, 0, bytesRead);
					dataOut.flush();

				}
				System.out.println("Commande GET exécutée");

				fis.close();
			}
			dataOut.close();
			socket_get.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
