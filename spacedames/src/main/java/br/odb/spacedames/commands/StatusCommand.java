package br.odb.spacedames.commands;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserMetaCommandLineAction;
import br.odb.spacedames.SpaceDamesMainApp;

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
		
		SpaceDamesMainApp game = (SpaceDamesMainApp) app;
		
		String buffer = "";
		
		for ( int c = 0; c < SpaceDamesMainApp.BOARD_SIZE; ++c ) {
			buffer = "";
			for( int d = 0; d < SpaceDamesMainApp.BOARD_SIZE; ++d ) {
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
