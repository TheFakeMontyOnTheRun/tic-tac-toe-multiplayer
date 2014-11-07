package br.odb.knights;


public class TurleKnight extends Knight {
	public TurleKnight() {
		super( 30, 6 );
	}
	
	@Override
	public String toString() {
	
		return "Turtle Knight - " + super.toString();
	}

	@Override
	public String getChar() {
		return String.valueOf( TileType.SPAWNPOINT_TURTLE );
	}
}
