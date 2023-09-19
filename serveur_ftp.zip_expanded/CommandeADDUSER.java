import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class CommandeADDUSER extends Commande {

	public CommandeADDUSER(PrintStream ps, String commandeStr, CommandExecutor comex) {
		super(ps, commandeStr, comex);
	}

	@Override
	public void execute() {
		System.out.println("Éxécution de la commande ADDUSER ");
		if (comex.var_usr.equals("admin")) {
			File dir = new File(comex.repertoire_absolu + "/" + commandeArgs[0]);
			if (!dir.exists()) {
				boolean created = dir.mkdir();
				if (created) {
					String filename = "pw.txt";
					String content = commandeArgs[1];

					try {
						FileWriter fileWriter = new FileWriter(
								comex.repertoire_absolu + "/" + commandeArgs[0] + "/" + filename);
						fileWriter.write(content);
						fileWriter.close();

						String[] tab_tempo = new String[comex.list.length];
						System.arraycopy(comex.list, 0, tab_tempo, 0, comex.list.length);
						tab_tempo[tab_tempo.length - 1] = commandeArgs[0];
						comex.list = tab_tempo;

						ps.println("0 Le User " + commandeArgs[0] + " a été créé avec succès son mot de passe est : "
								+ commandeArgs[1]);

					} catch (IOException e) {
						ps.println("2 Une erreur est survenue lors de la création du fichier : " + e.getMessage());
					}
				}
			} else {
				ps.println("2 Erreur lors de la création du répertoire.");
			}
		} else {
			ps.println("2 Vous n'êtes pas administrateur");
		}
	}

}
