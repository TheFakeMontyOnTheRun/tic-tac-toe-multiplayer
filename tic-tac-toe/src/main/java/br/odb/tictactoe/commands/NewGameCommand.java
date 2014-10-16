package br.odb.tictactoe.commands;


import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.tictactoe.TicTacToeMainApp;


public class NewGameCommand extends UserCommandLineAction {

	public NewGameCommand( TicTacToeMainApp app) {
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
		TicTacToeMainApp game = (TicTacToeMainApp) app;
		game.newGame();
	}

	@Override
	public String toString() {
		return "new-game";
	}

}
