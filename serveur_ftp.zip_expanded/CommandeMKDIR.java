import java.io.File;
import java.io.PrintStream;

public class CommandeMKDIR extends Commande {

	public CommandeMKDIR(PrintStream ps, String commandeStr, CommandExecutor comex) {
		super(ps, commandeStr, comex);
	}

	public void execute() {

		try {

			System.out.println("Exécution de la commande RMDIR");
			// on recupere le nom du repertoire
			File dir = new File(commandeArgs[0]);

			File path_dir = new File(comex.repertoireCourant + "/" + dir.getName());

			// on verifie si le repertoire exsit
			if (!path_dir.exists()) {
				if ((!path_dir.isDirectory())) {
					path_dir.mkdir();
					ps.println("0 Création de votre repertoire valide ! Le chemin est : " + comex.chemin_absolu + "/"
							+ commandeArgs[0]);

				} else {
					ps.println("2 Erreur dans la création du repertoire !");
				}
			} else {
				ps.println("2 Un repertoire du même nom existe deja ! ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}