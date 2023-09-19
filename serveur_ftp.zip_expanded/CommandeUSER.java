import java.io.PrintStream;

public class CommandeUSER extends Commande {

	public CommandeUSER(PrintStream ps, String commandeStr, CommandExecutor exec) {
		super(ps, commandeStr, exec);
	}

	public void execute() {
		// Ce serveur accepte uniquement le user personne
		boolean bool = false;
		String[] tab_tempo = comex.list;
		for (String s : tab_tempo) {

			if (commandeArgs[0].toLowerCase().equals(s)) {
				bool = true;
			}

		}
		if (bool) {
			comex.userOk = true;
			comex.var_usr = commandeArgs[0];
			comex.repertoireCourant = comex.repertoire_absolu + "/" + comex.var_usr;
			comex.chemin_absolu = comex.var_usr;

			System.out.println("nouveau chemin_absolu : " + comex.chemin_absolu);
			System.out.println("var_usr : " + comex.var_usr);
			System.out.println("repertoire absolu : " + comex.repertoire_absolu);

			ps.println("0 Commande user OK");
		} else {
			ps.println("2 Le user " + commandeArgs[0] + " n'existe pas");
		}

	}

}
