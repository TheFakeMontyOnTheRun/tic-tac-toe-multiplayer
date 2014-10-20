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
	
	public Game( int gameId ) {
		
		this.id = gameId;
		this.currentTeam = 0;
		this.winnerTeam = 0;
	}

	public abstract void checkForGameEnd() ;

	public abstract void sendMove(HashMap<String, String> params);

	public abstract void writeState(Writer writer);
}
