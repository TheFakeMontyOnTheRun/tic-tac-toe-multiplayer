package br.odb.multiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.odb.fpsdemo.FPSDemoGame;
import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.ServerContext;
import br.odb.multiplayer.model.spacedames.SpaceDamesGame;
import br.odb.multiplayer.model.tictactoe.TicTacToeGame;
import br.odb.vintage.dto.GameIdDTO;

/**
 * Servlet implementation class FindGame
 */

@WebServlet("/GetJavaGameId")
public class GetJavaGameId extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServerContext context = ServerContext
				.createOrRetrieve((ServletContext) getServletContext());

		int playerId;
		int gameType = Integer.parseInt(request.getParameter("gameType"));
		Game g = getGameNewOrVacantGame(context, gameType);
		playerId = g.addNewPlayer();
		GameIdDTO gis = new GameIdDTO( g.id, playerId, playerId );
		ObjectOutputStream oos = new ObjectOutputStream( response.getOutputStream() );
		oos.writeObject( gis );
	}

	private Game getGameNewOrVacantGame(ServerContext context, int gameType) {

		int bigger = 0;
		Game toReturn;
			// find a existing game pending for new players
		for (Game g :  context.games.values() ) {
			
			if ( g.type != gameType ) {
				continue;
			}
			
			if (g.players.size() < g.getNumberOfRequiredPlayers()
					&& !g.isTooOld()) {
				return g;
			}
	
			if (g.id > bigger) {
				bigger = g.id;
			}
		}

		if (gameType == 1) {
			toReturn = new TicTacToeGame(bigger + 1);
		} else if (gameType == 2) {
			toReturn = new SpaceDamesGame(bigger + 1);
		} else {
			toReturn = new FPSDemoGame(bigger + 1);
			System.out.println( "created new game with id " + ( bigger + 1 ) );
		}
		
		context.games.put( bigger + 1, toReturn );

		return toReturn;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}
}
