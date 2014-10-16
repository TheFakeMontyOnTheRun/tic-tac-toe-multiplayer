package br.odb.multiplayer.model;

import java.util.HashMap;

import javax.servlet.ServletContext;

public class ServerContext {
	public final HashMap<Integer, Game> games = new HashMap<Integer, Game>();

	public static ServerContext createOrRetrieve( ServletContext servletContext) {

		ServerContext context = (ServerContext) servletContext
				.getAttribute("games-context");

		if (context == null) {
			reset( servletContext );
		}

		return context;
	}

	public static void reset(ServletContext servletContext) {
		servletContext.setAttribute("games-context", new ServerContext() );
	}
}
