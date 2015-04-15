package br.odb.multiplayer.model.spacedames;

import br.odb.utils.math.Vec2;

public class PieceMovement {
	public Piece piece;
	final public Vec2 from = new Vec2();
	final public Vec2 to = new Vec2();
	
	@Override
	public String toString() {
		String buffer = "";
		
		buffer += (( int ) from.x );
		buffer += ",";
		buffer += (( int ) from.y );
		buffer += ",";
		buffer += (( int ) to.x );
		buffer += ",";
		buffer += (( int ) to.y );

		
		return buffer;
	}
}
