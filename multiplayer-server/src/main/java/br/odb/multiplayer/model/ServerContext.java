package br.odb.multiplayer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

public class ServerContext {

	public final Map<Integer, Game> games = new HashMap<Integer, Game>();
	
	private final Map<Game, List<Integer>> playerInGames = new HashMap<Game, List<Integer>>();

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

	public Game getGameForPlayerId(String playerId) {

		int pId = Integer.parseInt(playerId);
		
		for ( Game g : playerInGames.keySet() ) {
			for ( Integer candidateId : playerInGames.get(g) ) {
				if (candidateId.intValue() == pId ) {
					return g;
				}
			}
		}
		return null;
	}

	public void registerPlayerForGame(int playerId, Game g) {
		
		if (!playerInGames.containsKey(g)) {
			playerInGames.put(g, new ArrayList<Integer>());
		}
		
		playerInGames.get(g).add(playerId);
		
	}
}
