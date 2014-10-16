package br.odb.tictactoe.commands;


import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.tictactoe.TicTacToeMainApp;
import br.odb.tictactoe.TicTacToeMainApp.Piece;


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
		TicTacToeMainApp game = (TicTacToeMainApp) app;
		String[] operand = operands.split( "[ ]+" );
		
		if ( game.currentTeam != game.localPlayerTeam ) {
			app.getClient().printNormal( "not your turn" );
			return;
		}
		
		if( operand.length >= 2 ) {
			
			int x;
			int y;
			x = Integer.parseInt( operand[ 0 ] );
			y = Integer.parseInt( operand[ 1 ] );

			if ( x >= 0 && x < 3 && y >= 0 && y < 3 ) {
				
				if ( game.board[ y ][ x ] == Piece.EMPTY ) {
					
					game.board[ y ][ x ] = game.localPlayerTeam;
					game.sendRemoteMove( x, y );
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
