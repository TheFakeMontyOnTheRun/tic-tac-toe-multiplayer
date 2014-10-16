package br.odb.tictactoe.commands;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserMetaCommandLineAction;
import br.odb.tictactoe.TicTacToeMainApp;

public class StatusCommand extends UserMetaCommandLineAction {

	public StatusCommand( ConsoleApplication app ) {
		super( app );

	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requiredOperands() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		
		TicTacToeMainApp game = (TicTacToeMainApp) app;
		
		String buffer = "";
		
		for ( int c = 0; c < 3; ++c ) {
			buffer = "";
			for( int d = 0; d < 3; ++d ) {
				buffer += game.board[ c ][ d ];
			}
			app.getClient().printNormal( buffer );
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "status";
	}

}
