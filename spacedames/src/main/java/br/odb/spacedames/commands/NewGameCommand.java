package br.odb.spacedames.commands;


import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.spacedames.SpaceDamesMainApp;


public class NewGameCommand extends UserCommandLineAction {

	public NewGameCommand( SpaceDamesMainApp app) {
		super( );
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 1;
	}

	@Override
	public void run(ConsoleApplication app, String operand ) throws Exception {
		SpaceDamesMainApp game = (SpaceDamesMainApp) app;
		game.newGame();
	}

	@Override
	public String toString() {
		return "new-game";
	}

}
