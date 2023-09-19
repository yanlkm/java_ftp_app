import java.io.PrintStream;

public class CommandExecutor {

	String var_usr = "";
	String chemin_absolu = var_usr;
	String repertoire_absolu = System.getProperty("user.dir");
	String repertoireCourant = System.getProperty("user.dir");// +"/"+var_usr;
	public String[] list = { "admin", "adrien", "usr1", "usr2", "usr3" };

	protected boolean userOk = false;
	protected boolean pwOk = false;

	public void executeCommande(PrintStream ps, String commande) {
		if (userOk && pwOk) {
			// Changer de repertoire. Un (..) permet de revenir au repertoire superieur
			if (commande.split(" ")[0].equals("cd"))
				(new CommandeCD(ps, commande, this)).execute();

			// Telecharger un fichier
			if (commande.split(" ")[0].equals("get"))
				(new CommandeGET(ps, commande, this)).execute();

			// Afficher la liste des fichiers et des dossiers du repertoire courant
			if (commande.split(" ")[0].equals("ls"))
				(new CommandeLS(ps, commande, this)).execute();

			// Afficher le repertoire courant
			if (commande.split(" ")[0].equals("pwd"))
				(new CommandePWD(ps, commande, this)).execute();

			// Envoyer (uploader) un fichier
			if (commande.split(" ")[0].equals("stor"))
				(new CommandeSTOR(ps, commande, this)).execute();
			// ajouter un user
			if (commande.split(" ")[0].equals("adduser"))
				(new CommandeADDUSER(ps, commande, this)).execute();
			// supprimer un repertoire vide
			if (commande.split(" ")[0].equals("rmdir"))
				(new CommandeRMDIR(ps, commande, this)).execute();
			// creer un repertoire
			if (commande.split(" ")[0].equals("mkdir"))
				(new CommandeMKDIR(ps, commande, this)).execute();
			else {
				return;
			}

		} else {

			if (commande.split(" ")[0].equals("user") || commande.split(" ")[0].equals("pass")) {
				// Le login pour l'authentification
				if (commande.split(" ")[0].equals("user")) {
					(new CommandeUSER(ps, commande, this)).execute();
				}
				// Le mot de passe pour l'authentification
				if (commande.split(" ")[0].equals("pass")) {
					(new CommandePASS(ps, commande, this)).execute();
				}

			} else {
				ps.println("2 Vous n'êtes pas connecté !");
			}

		}

	}
}
