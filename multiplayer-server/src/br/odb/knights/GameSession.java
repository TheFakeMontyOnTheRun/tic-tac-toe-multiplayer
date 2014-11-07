/**
 * 
 */
package br.odb.knights;

/**
 * @author monty
 *
 */
public class GameSession {
	byte currentLevel;
	byte dificulty;
	
	public GameSession() {
		currentLevel = 0;
		dificulty = 0;
	}
	
	public void close() {
		
	}

	public GameLevel obtainCurrentLevel( byte level ) {
		// obtem ou cria o level corrente
		GameLevel toReturn = null;
		currentLevel = level;
		toReturn = GameLevelLoader.loadLevel( currentLevel );
		toReturn.setDificulty( dificulty );
		toReturn.reset();
		
		return toReturn;
	}

	public GameLevel obtainCurrentLevel(String data, GameLevel level ) {
		// obtem ou cria o level corrente
		GameLevel toReturn = null;
		toReturn = GameLevelLoader.loadLevel( data, level );
		toReturn.reset();
		
		return toReturn;
	}
}
