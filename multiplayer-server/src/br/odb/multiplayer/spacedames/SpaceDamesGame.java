package br.odb.multiplayer.spacedames;

import java.io.IOException;
import java.io.Writer;

import br.odb.multiplayer.model.Game;

public class SpaceDamesGame extends Game {

	public final int[][] table = new int[8][];

	
	public SpaceDamesGame(int gameId) {
		super(gameId);

		for (int c = 0; c < table.length; ++c) {
			table[c] = new int[8];
		}
	}

	@Override
	public void checkForGameEnd() {

	}

	@Override
	public void writeState(Writer writer) {
		
		StringBuilder sb = new StringBuilder( "<?xml version='1.0'?><game><state>" );
		
		try {
			for (int c = 0; c < 8; ++c) {
				for (int d = 0; d < 8; ++d) {
					sb.append(table[c][d]);
				}
			}

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
		return 2;
	}

}
