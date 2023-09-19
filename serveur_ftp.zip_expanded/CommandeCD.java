import java.io.File;
import java.io.PrintStream;

public class CommandeCD extends Commande {

	public CommandeCD(PrintStream ps, String commandeStr, CommandExecutor comex) {
		super(ps, commandeStr, comex);
	}

	public void execute() {
		System.out.println("Exécution de la commande CD");
		// Vérifier si le premier argument est présent
		if (commandeArgs.length < 1) {

			// ps.println("2 La commande CD doit avoir un argument");

			String chemin_absolu2 = comex.var_usr;

			File dir = new File(comex.repertoire_absolu + "/" + chemin_absolu2);

			if (!dir.isDirectory() || !dir.canRead()) {
				ps.println("2 Le répertoire " + chemin_absolu2 + " n'existe pas ou ne peut pas être lu");
				return;
			}

			// Mettre à jour le répertoire courant
			comex.chemin_absolu = comex.var_usr;

			comex.repertoireCourant = comex.repertoire_absolu + "/" + chemin_absolu2;

			ps.println("0 Répertoire courant changé en " + chemin_absolu2);
			// ps.println(CommandExecutor.repertoireCourant);

		} else

		{
			String path = commandeArgs[0].toString();

			if (path.equals("..")) {

				String currentDir = comex.chemin_absolu;
				int debut = currentDir.indexOf("/");
				int fin = currentDir.lastIndexOf("/");

				if (debut == -1 && fin == -1) {
					ps.println("2 Erreur: vous êtes déjà à la racine.");
				}

				else {

					String test_currentDir = currentDir.substring(0, fin);

					File chemin = new File(comex.repertoire_absolu + "/" + test_currentDir);

					if (chemin.isDirectory()) {
						comex.repertoireCourant = chemin.toString();
						comex.chemin_absolu = test_currentDir;
						ps.println("0 retour au repertoire précédent l'utilisateur : " + test_currentDir);

					} else {

					}

				}
			}

			else {

				if (path.startsWith("../"))

				{

					String currentDir = comex.chemin_absolu;
					int debut = currentDir.indexOf("/");
					int fin = currentDir.lastIndexOf("/");
				
					if (debut == -1 && fin == -1) {
						ps.println("2 Erreur: vous êtes déjà à la racine.");
					} else {
					
						currentDir = currentDir.substring(0, fin);
						String path_changed = path.substring(3, path.length());
						
						String test_currentDir = currentDir + "/" + path_changed;
						
						if((path_changed.startsWith("/"))||(path_changed.startsWith(".."))||(path_changed.startsWith("/..")))
						{
							ps.println("2 Chemin incorrect : " + currentDir + "/" + path_changed);
						
						}
						else {
							File chemin = new File(comex.repertoire_absolu + "/" + test_currentDir);
							
							
							if ((chemin.isDirectory()) && (chemin.exists()) ) {
								comex.repertoireCourant = chemin.toString();

								comex.chemin_absolu = test_currentDir;

								ps.println("0 Nouveau chemin courant :" + test_currentDir);

							} else {
								ps.println("2 Chemin incorrect : " + test_currentDir);
							}
							
						}

					}

				} else {

					String currentDir = comex.chemin_absolu;
					File file = new File(comex.repertoire_absolu + "/" + currentDir + "/" + path);
					if (file.isDirectory()) {
						comex.chemin_absolu = currentDir + "/" + path;
						comex.repertoireCourant = comex.repertoire_absolu + "/" + comex.chemin_absolu;

						ps.println("0 nouveau chemin courant :" + comex.chemin_absolu);

					} else {

						ps.println("2 Chemin incorrect : " + comex.repertoire_absolu + "/" + currentDir + "/" + path);
					}

				}

			}

		}

	}

}
