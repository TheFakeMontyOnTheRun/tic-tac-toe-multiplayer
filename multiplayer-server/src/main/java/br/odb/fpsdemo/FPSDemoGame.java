/**
 * 
 */
package br.odb.fpsdemo;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.odb.multiplayer.model.Game;
import br.odb.multiplayer.model.Player;
import br.odb.vintage.dto.BeamStateDTO;
import br.odb.vintage.dto.FPSGameStatusDTO;
import br.odb.vintage.dto.PlayerStateDTO;

/**
 * @author monty
 *
 */
public class FPSDemoGame extends Game {

	/**
	 * @param gameId
	 */
	public FPSDemoGame(int gameId) {
		super(gameId, 3);
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
			List<PlayerStateDTO> playerStates = new ArrayList<>();
			PlayerStateDTO[] rawStates;
			
			//System.out.println( "------" );
			for ( Player state : players.values() ) {
				PlayerState fpsState = (PlayerState) state;
				playerStates.add( new PlayerStateDTO( "" + fpsState.playerId, fpsState.position, fpsState.angleXZ, -1.0f ) );
		//		System.out.println( "player id: " + fpsState.id + " position: " + fpsState.position + " angle: " + fpsState.angleXZ );
			}
			
			rawStates = playerStates.toArray( new PlayerStateDTO[0] );
			
			oos.writeObject( new FPSGameStatusDTO(  rawStates, new BeamStateDTO[]{} ) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Player makeNewPlayer() {
		
		int biggerId = 0;
		
//		for ( Integer i : players.keySet() ) {
//			System.out.println( "key: " + i + " -> " + players.get( i ) );
//		}

		
		for ( Player p : players.values() ) {
			if ( biggerId < p.playerId ) {
				biggerId = p.playerId;
			}
		}
		
		++biggerId;
		System.out.println( "Adding player with id " + biggerId );
		return new PlayerState(biggerId, id, biggerId, "");
	}
	

	/* (non-Javadoc)
	 * @see br.odb.multiplayer.model.Game#getNumberOfRequiredPlayers()
	 */
	@Override
	public int getNumberOfRequiredPlayers() {
		return 20;
	}

	@Override
	public void sendMove(HashMap<String, String> params) {
		
		String parameter = params.get( "playerId" );

//		System.out.println( "parameter: " + parameter );
		
		int playerId = Integer.parseInt( parameter );
		
//		System.out.println( "receiving id for player " + playerId );
		
		float x = Float.parseFloat( params.get( "x" ) );
		float y = Float.parseFloat( params.get( "y" ) );
		float z = Float.parseFloat( params.get( "z" ) );
		float a = Float.parseFloat( params.get( "a" ) );
		
		
//		for ( Integer i : players.keySet() ) {
//			System.out.println( "key: " + i + " -> " + players.get( i ) );
//		}
		
		PlayerState player = (PlayerState) players.get( playerId );
		
		player.angleXZ = a;		
		player.position.set( x, y, z );
	}
}
