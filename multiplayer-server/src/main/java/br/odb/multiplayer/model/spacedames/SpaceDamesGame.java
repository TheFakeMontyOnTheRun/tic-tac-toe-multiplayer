package br.odb.multiplayer.model.spacedames;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import br.odb.multiplayer.model.Game;
import br.odb.utils.math.Vec2;

public class SpaceDamesGame extends Game {

	GameBoard board = new GameBoard();
	
	public SpaceDamesGame(int gameId) {
		super(gameId);
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

		board.move( new Vec2( decodedX0, decodedY0 ), new Vec2( decodedX1, decodedY1 ) );
	}

	public void writeState(OutputStream os) {
		
		StringBuilder sb = new StringBuilder( "<?xml version='1.0'?><game><state>" );
		
		
		try {
			sb.append( board.toString() );
			sb.append( "</state><current>" );
			
			sb.append(players.get(currentTeam).teamId);
			sb.append( "</current><winner>" );
			sb.append(winnerTeam);
			sb.append( "</winner></game>" );

			os.write( sb.toString().getBytes() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getNumberOfRequiredPlayers() {
		return 2;
	}
}