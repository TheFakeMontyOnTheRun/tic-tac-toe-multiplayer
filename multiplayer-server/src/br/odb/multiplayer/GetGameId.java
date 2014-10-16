package br.odb.multiplayer;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.Player;
import br.odb.multiplayer.model.ServerContext;

/**
 * Servlet implementation class FindGame
 */
@WebServlet("/GetGameId")
public class GetGameId extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetGameId() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServerContext context = ServerContext
				.createOrRetrieve(  (ServletContext) getServletContext() );
		
		int bigger = 0;
		int playerId;
		
		//find a existing game pending for new players
		for ( Game g : context.games.values() ) {
			if ( g.players.size() < 2 ) {

				response.getOutputStream().write( (byte)g.id );
				g.players.add( new Player( g.id, g.players.size() + 1, g.players.size() + 1, "" ) );
				playerId = g.players.size();// + 1;
				response.getOutputStream().write( (byte)playerId );
//				response.getOutputStream().write( (byte) playerId + 1 );		
				response.getOutputStream().write( (byte)playerId );		

				
				return;
			}
			
			if ( g.id > bigger ) {
				bigger = g.id;
			}
		}		
		
		//create new game
		Game g = new Game( bigger + 1 );
		context.games.put( g.id, g );
		
		response.getOutputStream().write( (byte)g.id );
		g.players.add( new Player( g.id, g.players.size() + 1, g.players.size() + 1, "" ) );
		playerId = g.players.size();// + 1;
		response.getOutputStream().write( (byte)playerId );
//		response.getOutputStream().write( (byte) playerId + 1 );		
		response.getOutputStream().write( (byte)playerId );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
