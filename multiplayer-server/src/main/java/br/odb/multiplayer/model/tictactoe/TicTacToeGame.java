package br.odb.multiplayer.model.tictactoe;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.Player;

public class TicTacToeGame extends Game {

	public final int[][] table = new int[3][];

	public TicTacToeGame(int gameId) {

		super(gameId, 1);

		for (int c = 0; c < 3; ++c) {
			table[c] = new int[3];
		}
	}

	@Override
	public void checkForGameEnd() {

		for (int c = 0; c < 3; ++c) {

			if (table[c][0] == table[c][1] && table[c][2] == table[c][1]) {
				winnerPlayerId = table[c][0];
				return;
			}

			if (table[0][c] == table[1][c] && table[2][c] == table[1][c]) {
				winnerPlayerId = table[0][c];
				return;
			}
		}

		if (table[0][0] == table[1][1] && table[2][2] == table[1][1]) {
			winnerPlayerId = table[0][0];
			return;
		}

		if (table[0][2] == table[1][1] && table[2][0] == table[1][1]) {
			winnerPlayerId = table[0][2];
			return;
		}
	}

	@Override
	public void sendMove(HashMap<String, String> params) {
		String x = params.get("x");
		String y = params.get("y");

		int decodedX = Integer.parseInt(x);
		int decodedY = Integer.parseInt(y);
		int playerId = Integer.parseInt(params.get("playerId"));

		table[decodedY][decodedX] = playerId;

		setTheNextPlayerAsCurrent();
	}

	public void writeState(OutputStream os) {

		StringBuilder sb = new StringBuilder("<?xml version='1.0'?><game><state>");

		try {
			for (int c = 0; c < 3; ++c) {
				for (int d = 0; d < 3; ++d) {
					sb.append(table[c][d]);
				}
			}

			System.out.println("writing status for " + currentPlayerId);

			Player p = players.get(currentPlayerId);

			if (p != null) {
				sb.append("</state><current>");
				sb.append(p.playerId);
				sb.append("</current><winner>");
				sb.append(winnerPlayerId);
				sb.append("</winner></game>");
			} else {
				System.out.println("current player is null!");
			}

			os.write(sb.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getNumberOfRequiredPlayers() {
		return 2;
	}

	@Override
	public int getNumberOfMaximumPlayers() {
		return 2;
	}
}
