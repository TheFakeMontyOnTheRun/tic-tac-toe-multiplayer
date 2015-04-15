package br.odb.spacedames.commands;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.spacedames.SpaceDamesMainApp;

public class ReceiveCommand extends UserCommandLineAction {

	int current = 0;
	
	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
		SpaceDamesMainApp game = (SpaceDamesMainApp) app;
		game.receiveStatus();
	}

	@Override
	public String toString() {
		return "receive";
	}

}
