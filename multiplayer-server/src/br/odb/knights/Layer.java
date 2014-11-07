package br.odb.knights;

import java.util.ArrayList;

import br.odb.utils.math.Vec2;

public class Layer {
	final public ArrayList< GameObject > children = new ArrayList< GameObject >();
	public final Vec2 position = new Vec2();
	public final Vec2 size = new Vec2();
	
	public void add( GameObject d ) {
		children.add( d );
	}
	
	public void clear() {
		children.clear();
	}
}
