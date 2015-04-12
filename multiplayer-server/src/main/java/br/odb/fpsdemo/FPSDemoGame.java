/**
 * 
 */
package br.odb.fpsdemo;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.Player;

/**
 * @author monty
 *
 */
public class FPSDemoGame extends Game {

	/**
	 * @param gameId
	 */
	public FPSDemoGame(int gameId) {
		super(gameId);
	}

	/* (non-Javadoc)
	 * @see br.odb.multiplayer.model.Game#checkForGameEnd()
	 */
	@Override
	public void checkForGameEnd() {
	}

	/* (non-Javadoc)
	 * @see br.odb.multiplayer.model.Game#writeState(java.io.Writer)
	 */
	@Override
	public void writeState(OutputStream os) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream( os );
			oos.writeObject( players );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Player makeNewPlayer() {
	
		return new PlayerState(id, players.size() + 1, players.size() + 1, "");
	}
	

	/* (non-Javadoc)
	 * @see br.odb.multiplayer.model.Game#getNumberOfRequiredPlayers()
	 */
	@Override
	public int getNumberOfRequiredPlayers() {
		return 2;
	}

	@Override
	public void sendMove(HashMap<String, String> params) {
		
		int playerId = Integer.parseInt( params.get( "id" ) );
		
		float x = Float.parseFloat( params.get( "x" ) );
		float y = Float.parseFloat( params.get( "y" ) );
		float z = Float.parseFloat( params.get( "z" ) );
		float a = Float.parseFloat( params.get( "a" ) );
		
		PlayerState player = (PlayerState) players.get( playerId );
		
		player.angleXZ = a;
		
		player.position.set( x, y, z );
	}
}
