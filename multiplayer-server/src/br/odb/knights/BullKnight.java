package br.odb.knights;

public class BullKnight extends Knight {
	public BullKnight() {
		super( 20, 14 );
	}
	
	@Override
	public String toString() {
	
		return "Bull Knight - " + super.toString();
	}

	@Override
	public String getChar() {
		return String.valueOf( TileType.SPAWNPOINT_BULL );
	}
}
