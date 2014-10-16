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

/**
 * Servlet implementation class SendMove
 */
@WebServlet("/SendMove")
public class SendMove extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMove() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServerContext context = ServerContext
				.createOrRetrieve(  (ServletContext) getServletContext() );

		Game g = context.games.get( Integer.parseInt( request.getParameter( "gameId" ) ) );
		
		if ( g.winnerTeam != 0 ) {
			return;
		}

		String x = request.getParameter( "x" );
		String y = request.getParameter( "y" );
		
		int decodedX = Integer.parseInt( x );
		int decodedY = Integer.parseInt( y );
		
		g.table[ decodedY ][ decodedX ] = Integer.parseInt( request.getParameter( "playerId" ) );
		g.currentTeam = ( g.currentTeam + 1) % g.players.size();
		g.checkForGameEnd();
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
