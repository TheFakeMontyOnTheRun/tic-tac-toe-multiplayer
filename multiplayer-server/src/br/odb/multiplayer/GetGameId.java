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
import br.odb.multiplayer.model.tictactoe.TicTacToeGame;
import br.odb.multiplayer.spacedames.SpaceDamesGame;

/**
 * Servlet implementation class FindGame
 */
@WebServlet("/GetGameId")
public class GetGameId extends HttpServlet {
	
	class GameIdResponse {
		private int id;
		private int playerId;
		private int teamId;

		@Override
		public String toString() {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append( "<?xml version='1.0'?>\n<game><gameId>" );
			sb.append( id );
			sb.append( "</gameId><playerId>" );
			sb.append( playerId );
			sb.append( "</playerId><teamId>" );
			sb.append( teamId );		
			sb.append( "</teamId></game>" );
			
			return sb.toString();
		}
	}
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetGameId() {
        super();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServerContext context = ServerContext
				.createOrRetrieve(  (ServletContext) getServletContext() );
				
		int playerId;
		int gameType = Integer.parseInt( request.getParameter( "gameType" ) );
		GameIdResponse gis = new GameIdResponse();
		Game g = getGameNewOrVacantGame( context, gameType );
		
		playerId = g.addNewPlayer();

		gis.id = g.id;
		gis.playerId = playerId;
		gis.teamId = playerId;
		
		response.getOutputStream().write( gis.toString().getBytes() );

	}

	private Game getGameNewOrVacantGame( ServerContext context, int gameType ) {
		
		int bigger = 0;
		
		Game toReturn;
		//find a existing game pending for new players
		for ( Game g : context.games.values() ) {
			if ( g.players.size() < g.getNumberOfRequiredPlayers() && !g.isTooOld() ) {				
				return g;
			}
			
			if ( g.id > bigger ) {
				bigger = g.id;
			}
		}
		
		if ( gameType == 1 ) {
			toReturn = new TicTacToeGame( bigger + 1 );
		} else {
			toReturn = new SpaceDamesGame( bigger + 1 );
		}
			
		context.games.put( toReturn.id, toReturn );
		
		return toReturn;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
