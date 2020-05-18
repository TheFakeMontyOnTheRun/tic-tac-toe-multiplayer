package br.odb.multiplayer;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.ServerContext;

@WebServlet(urlPatterns = "/GetGameStatus")
public class GetGameStatus extends HttpServlet {

	private static final long serialVersionUID = -3789801483661712651L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServerContext context = ServerContext.createOrRetrieve((ServletContext) getServletContext());

		OutputStream os = response.getOutputStream();
		String playerId = request.getParameter("playerId");
		Game g = context.getGameForPlayerId(playerId);

		if (g != null) {
			g.writeState(playerId, os);
		} else {
			System.out.println("game is null!");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
