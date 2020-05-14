package br.odb.multiplayer.model;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

public class ServerContext {

	public final Map<Integer, Game> games = new HashMap<Integer, Game>();

	public final Map<String, Game> gameBuilders = new HashMap<String, Game>();

	public static ServerContext createOrRetrieve(ServletContext servletContext) {

		ServerContext context = (ServerContext) servletContext.getAttribute("games-context");

		if (context == null) {
			reset(servletContext);

			context = (ServerContext) servletContext.getAttribute("games-context");
		}

		return context;
	}

	public static void reset(ServletContext servletContext) {
		servletContext.setAttribute("games-context", new ServerContext());
	}
}
