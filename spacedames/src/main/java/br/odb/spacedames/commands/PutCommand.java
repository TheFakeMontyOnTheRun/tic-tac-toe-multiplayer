package br.odb.spacedames.commands;


import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.spacedames.SpaceDamesMainApp;
import br.odb.spacedames.SpaceDamesMainApp.Piece;


public class PutCommand extends UserCommandLineAction {

	
	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 2;
	}

	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
		SpaceDamesMainApp game = (SpaceDamesMainApp) app;
		String[] operand = operands.split( "[ ]+" );
		
		if ( game.currentTeam != game.localPlayerTeam ) {
			app.getClient().printNormal( "not your turn" );
			return;
		}
		
		if( operand.length >= 4 ) {
			
			int x0;
			int y0;
			int x1;
			int y1;

			x0 = Integer.parseInt( operand[ 0 ] );
			y0 = Integer.parseInt( operand[ 1 ] );
			x1 = Integer.parseInt( operand[ 2 ] );
			y1 = Integer.parseInt( operand[ 3 ] );


			if ( 
			     x0 >=0 &&
			     x0 < SpaceDamesMainApp.BOARD_SIZE &&
			     y0 >= 0 &&
			     y0 < SpaceDamesMainApp.BOARD_SIZE &&
			     x1 >=0 &&
			     x1 < SpaceDamesMainApp.BOARD_SIZE &&
			     y1 >= 0 &&
			     y1 < SpaceDamesMainApp.BOARD_SIZE 

				) {
				
				if ( game.board[ y0 ][ x0 ] != Piece.EMPTY &&
					game.board[ y1 ][ x1 ] == Piece.EMPTY 
					) {

					game.board[ y0 ][ x0 ] = Piece.EMPTY;
					game.board[ y1 ][ x1 ] = game.localPlayerTeam;
					game.sendRemoteMove( x0, y0, x1, y1 );
				} else {
					app.getClient().printNormal( "position taken" );
				}
			}
		}
	}

	@Override
	public String toString() {
		return "put";
	}
}
