package br.odb.fpsdemo;

import br.odb.multiplayer.model.Player;
import br.odb.utils.math.Vec3;

public class PlayerState extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5253098393916532147L;
	
	public PlayerState(int id, int gameId, int teamId, String address) {
		super(id, gameId, teamId, address);
		
		this.team = PlayerTeam.values()[ teamId ];
	}
	
	public final Vec3 position = new Vec3();
	public final PlayerTeam team;
	public float angleXZ;

}
