package br.odb.multiplayer;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.ServerContext;

/**
 * Servlet implementation class SendMove
 */
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServerContext context = ServerContext
				.createOrRetrieve((ServletContext) getServletContext());

		Game g = context.games.get(Integer.parseInt(request
				.getParameter("gameId")));

		if (g.winnerTeam != 0) {
			return;
		}

		Enumeration<String> parameterNames = request.getParameterNames();
		HashMap< String, String > params = new HashMap< String, String >();
		
		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();
			String paramValues = request.getParameter(paramName);
			params.put( paramName, paramValues );
		}
		
		g.lastMoveTime = System.currentTimeMillis();
		g.sendMove( params );		
		g.checkForGameEnd();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
