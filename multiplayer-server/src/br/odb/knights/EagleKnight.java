package br.odb.knights;

public class EagleKnight extends Knight {
	public EagleKnight() {
		super( 25, 10 );
	}
	
	@Override
	public String toString() {
		return "The Shadow - " + super.toString();
	}

	@Override
	public String getChar() {
		return String.valueOf( TileType.SPAWNPOINT_EAGLE );
	}
}
