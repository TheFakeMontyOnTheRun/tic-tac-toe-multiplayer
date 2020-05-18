package br.odb.multiplayer;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.ServerContext;
import br.odb.multiplayer.model.rpg.RPGGame;
import br.odb.multiplayer.model.tictactoe.TicTacToeGame;

@WebServlet(urlPatterns = "/GetGameId")
public class GetGameId extends HttpServlet {

	private static final long serialVersionUID = 4512362498041835138L;

	class GameIdResponse {

		private int gameId;
		private int playerId;

		@Override
		public String toString() {

			StringBuilder sb = new StringBuilder();

			sb.append("<?xml version='1.0'?>\n<game><gameId>");
			sb.append(gameId);
			sb.append("</gameId><playerId>");
			sb.append(playerId);
			sb.append("</playerId></game>");

			return sb.toString();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServerContext context = ServerContext.createOrRetrieve((ServletContext) getServletContext());

		int playerId;
		int gameType = Integer.parseInt(request.getParameter("gameType"));
		GameIdResponse gis = new GameIdResponse();
		Game g = getGameNewOrVacantGame(context, gameType);

		playerId = g.addNewPlayer();
		
		context.registerPlayerForGame( playerId, g);

		gis.gameId = g.gameId;
		gis.playerId = playerId;

		response.getOutputStream().write(gis.toString().getBytes());

	}

	private Game getGameNewOrVacantGame(ServerContext context, int gameType) {

		int bigger = 0;

		Game toReturn;

		// find a existing game pending for new players
		for (Game g : context.games.values()) {
			if ( g.getType() == gameType && g.players.size() < g.getNumberOfRequiredPlayers() && !g.isTooOld()) {
				System.out.println("player joining game with id " + (g.gameId));
				return g;
			}

			if (g.gameId > bigger) {
				bigger = g.gameId;
			}
		}

		switch( gameType ) {
			case 2:
				toReturn = new RPGGame(bigger + 1);
				break;
			case 1:
			default:
			toReturn = new TicTacToeGame(bigger + 1);
		}


		System.out.println("created new game with id " + toReturn.gameId);

		context.games.put(toReturn.gameId, toReturn);

		return toReturn;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
