package br.odb.multiplayer;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.ServerContext;
import br.odb.multiplayer.model.tictactoe.TicTacToeGame;

/**
 * Servlet implementation class CheckGameStatus
 */
@WebServlet("/GetGameStatus")
public class GetGameStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetGameStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServerContext context = ServerContext
				.createOrRetrieve(  (ServletContext) getServletContext() );
		
		Writer writer = response.getWriter();
		Game g = context.games.get( Integer.parseInt( request.getParameter( "gameId" ) ) );
		g.writeState( writer );
		writer.flush();		
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
