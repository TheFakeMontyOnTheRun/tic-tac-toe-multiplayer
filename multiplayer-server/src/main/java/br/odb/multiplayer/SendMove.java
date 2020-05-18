package br.odb.multiplayer;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.ServerContext;

@WebServlet(urlPatterns = "/SendMove")
public class SendMove extends HttpServlet {

	private static final long serialVersionUID = 6138518555798154506L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServerContext context = ServerContext.createOrRetrieve((ServletContext) getServletContext());		
		
		String playerId = request.getParameter("playerId");
		Game g = context.getGameForPlayerId(playerId);
		

		if (g.winnerPlayerId != 0) {
			return;
		}

		Enumeration<String> parameterNames = request.getParameterNames();
		HashMap<String, String> params = new HashMap<String, String>();

		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();
			String paramValues = request.getParameter(paramName);
			params.put(paramName, paramValues);
		}

		g.lastMoveTime = System.currentTimeMillis();
		g.sendMove(params);
		g.checkForGameEnd();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
