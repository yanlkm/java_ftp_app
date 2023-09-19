import java.io.File;
import java.io.PrintStream;

public class CommandeRMDIR extends Commande {

	public CommandeRMDIR(PrintStream ps, String commandeStr, CommandExecutor exec) {
		super(ps, commandeStr, exec);
	}

	public void execute() {

		try {
			// appel de la fonction rmdir
			System.out.println("Exécution de la commande RMDIR");
			// on recupere le nom du repertoire
			File dir = new File(commandeArgs[0]);

			File path_dir = new File(comex.repertoireCourant + "/" + dir.getName());

			// on verifie si le repertoire exsit
			if (path_dir.exists()) {
				if ((path_dir.isDirectory())) {

					if (path_dir.list().length == 0) {
						// si le rep existe, que c'est un repertoire, et que il n'a pas de contenu on
						// supprime
						path_dir.delete();
						ps.println("0 chemin du repertoire supprimé : " + comex.chemin_absolu + "/" + commandeArgs[0]);
					} else {
						ps.println("2 Votre repertoire à du contenu impossible de le supprimer");
					}
				} else {
					ps.println("2 Ceci n'est pas un repertoire");
				}
			} else {
				ps.println("2 Erreur Le repertoire n'existe pas");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
