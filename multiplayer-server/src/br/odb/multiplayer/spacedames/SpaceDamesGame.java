package br.odb.multiplayer.spacedames;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.spacedames.GameBoard.Team;
import br.odb.utils.math.Vec2;

public class SpaceDamesGame extends Game {

	
	GameBoard board;
	
	public final int[][] table = new int[8][];

	
	public SpaceDamesGame(int gameId) {
		super(gameId);

		board = new GameBoard( Team.BLACK );
		
		for (int c = 0; c < table.length; ++c) {
			table[c] = new int[8];
		}
	}

	@Override
	public void checkForGameEnd() {

	}
	
	@Override
	public void sendMove(HashMap<String, String> params) {
		String x0 = params.get("x0");
		String y0 = params.get("y0");
		String x1 = params.get("x1");
		String y1 = params.get("y1");

		int decodedX0 = Integer.parseInt(x0);
		int decodedY0 = Integer.parseInt(y0);
		int decodedX1 = Integer.parseInt(x1);
		int decodedY1 = Integer.parseInt(y1);
		
		PieceMovement move = new PieceMovement();
		
		move.from.set( decodedX0, decodedY0 );
		move.to.set( decodedX1, decodedY1 );
		
		board.trySelectingPiece( new Vec2( decodedX0, decodedY0 ) );
		board.move( move.from, move.to );
		
		int teamId = Integer.parseInt(params.get("playerId"));
		
//		if ( teamId != currentTeam ) {
//			return;
//		}

//		table.
		
		table[decodedY1][decodedX1] = teamId;
		table[decodedY0][decodedX0] = 0;
		currentTeam = (currentTeam + 1) % players.size();
	}
	

	@Override
	public void writeState(Writer writer) {
		System.out.println("write dames state");
		
		StringBuilder sb = new StringBuilder( "<?xml version='1.0'?><game><state>" );
		
		try {
			for (int c = 0; c < 8; ++c) {
				for (int d = 0; d < 8; ++d) {
//					sb.append(table[c][d]);
					sb.append( board.getTeamAt(d, c).ordinal() + 1 );
					System.out.print( "team at " + d + ", " + c + ": " + ( board.getTeamAt(d, c).ordinal() + 1 ) );
				}
			}
			System.out.println();
			sb.append( "</state><current>" );
			
			sb.append(players.get(currentTeam).teamId);
			sb.append( "</current><winner>" );
			sb.append(winnerTeam);
			sb.append( "</winner></game>" );

			writer.write( sb.toString() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getNumberOfRequiredPlayers() {
		return Team.values().length;
	}

}
