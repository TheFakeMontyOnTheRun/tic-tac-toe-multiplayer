package br.odb.multiplayer.model.tictactoe;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import br.odb.multiplayer.model.Game;

public class TicTacToeGame extends Game {

	public final int[][] table = new int[3][];

	public TicTacToeGame(int gameId) {
		super(gameId);

		for (int c = 0; c < 3; ++c) {
			table[c] = new int[3];
		}
	}

	@Override
	public void checkForGameEnd() {

		for (int c = 0; c < 3; ++c) {

			if (table[c][0] == table[c][1] && table[c][2] == table[c][1]) {
				winnerTeam = table[c][0];
				return;
			}

			if (table[0][c] == table[1][c] && table[2][c] == table[1][c]) {
				winnerTeam = table[0][c];
				return;
			}
		}

		if (table[0][0] == table[1][1] && table[2][2] == table[1][1]) {
			winnerTeam = table[0][0];
			return;
		}

		if (table[0][2] == table[1][1] && table[2][0] == table[1][1]) {
			winnerTeam = table[0][2];
			return;
		}

	}

	@Override
	public void sendMove(HashMap<String, String> params) {
		String x = params.get("x");
		String y = params.get("y");

		int decodedX = Integer.parseInt(x);
		int decodedY = Integer.parseInt(y);

		table[decodedY][decodedX] = Integer.parseInt(params.get("playerId"));
		currentTeam = (currentTeam + 1) % players.size();
	}

	public void writeState(Writer writer) {
		
		StringBuilder sb = new StringBuilder( "<?xml version='1.0'?><game><state>" );
		
		
		try {
			for (int c = 0; c < 3; ++c) {
				for (int d = 0; d < 3; ++d) {
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
