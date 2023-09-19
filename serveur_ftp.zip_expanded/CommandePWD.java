import java.io.PrintStream;

public class CommandePWD extends Commande {

	public CommandePWD(PrintStream ps, String commandeStr, CommandExecutor comex) {
		super(ps, commandeStr, comex);
	}

	public void execute() {
		System.out.println("Exécution de la commande PWD");
		String s = comex.chemin_absolu;

		ps.println("0 " + s);

	}

}
