package br.odb.knights;

import br.odb.knights.Actor;
import br.odb.utils.math.Vec2;

public class Tile extends GameObject {
	private TileType kind;
	private int myColor;
	private boolean block;
	public Actor ocupant;
	
	/**
	 * @return the block
	 */
	public boolean isBlock() {
		return block;
	}

	/**
	 * @param block the block to set
	 */
	public void setBlock(boolean block) {
		this.block = block;
	}

	/**
	 * @return the kind
	 */
	public TileType getKind() {
		return kind;
	}

	/**
	 * @param kind the kind to set
	 */
	public void setKind( TileType kind) {
		this.kind = kind;
		block = (kind != TileType.GRASS) && ( kind != TileType.DOOR );
	}

	/**
	 * @return the myColor
	 */
	public int getMyColor() {
		return myColor;
	}

	/**
	 * @param myColor the myColor to set
	 */
	public void setMyColor( int myColor) {
		this.myColor = myColor;
	}


	public Tile(int x, int y, TileType kind)
	{
		
		setKind(kind);
		position.set( new Vec2( x, y ) );
	}
}
