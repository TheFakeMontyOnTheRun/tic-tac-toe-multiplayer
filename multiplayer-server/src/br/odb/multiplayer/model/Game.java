package br.odb.multiplayer.model;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Game {
	
	public final ArrayList< Player > players = new ArrayList< Player >();
	
	public final int id;
	public int winnerTeam;
	public int currentTeam;
	public boolean playing;
	public long lastMoveTime;
	public static final long TIME_LIMIT = 2 * 60 * 1000;
	
	public Game( int gameId ) {
		
		this.id = gameId;
		this.currentTeam = 0;
		this.winnerTeam = 0;
		lastMoveTime = System.currentTimeMillis();
	}
	
	public int addNewPlayer() {
		lastMoveTime = System.currentTimeMillis();
		players.add( new Player( id, players.size() + 1, players.size() + 1, "" ) );
		
		return players.size();
	}

	public abstract void checkForGameEnd() ;

	public void sendMove(HashMap<String, String> params) {
		lastMoveTime = System.currentTimeMillis();
	}

	public abstract void writeState(Writer writer);
	
	public abstract int getNumberOfRequiredPlayers();

	public boolean isTooOld() {
		return ( System.currentTimeMillis() - lastMoveTime ) > TIME_LIMIT;
	}
}
