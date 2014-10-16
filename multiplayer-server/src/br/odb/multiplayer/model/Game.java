package br.odb.multiplayer.model;

import java.util.ArrayList;

public class Game {
	
	public final ArrayList< Player > players = new ArrayList< Player >();
	public final int id;
	public int winnerTeam;
	public int currentTeam;
	public boolean playing;
	public final int[][] table = new int[ 3 ][];
	
	public Game( int gameId ) {
		
		this.id = gameId;
		this.currentTeam = 0;
		this.winnerTeam = 0;
		
		for ( int c = 0; c < 3; ++c ) {
			table[ c ] = new int[ 3 ];
		}
	}

	public void checkForGameEnd() {
		
		for ( int c = 0; c < 3; ++c ) {
			
			if ( table[ c ][ 0 ] == table[ c ][ 1 ] && table[ c ][ 2 ] == table[ c ][ 1 ] ) {
				winnerTeam = table[ c ][ 0 ];
				return;
			}
			
			if ( table[ 0 ][ c ] == table[ 1 ][ c ] && table[ 2 ][ c ] == table[ 1 ][ c ] ) {
				winnerTeam = table[ 0 ][ c ];
				return;
			}
		}
		
		if ( table[ 0 ][ 0 ] == table[ 1 ][ 1 ] && table[ 2 ][ 2 ] == table[ 1 ][ 1 ] ) {
			winnerTeam = table[ 0 ][ 0 ];
			return;
		}
		
		if ( table[ 0 ][ 2 ] == table[ 1 ][ 1 ] && table[ 2 ][ 0 ] == table[ 1 ][ 1 ] ) {
			winnerTeam = table[ 0 ][ 2 ];
			return;
		}		
	}
}
