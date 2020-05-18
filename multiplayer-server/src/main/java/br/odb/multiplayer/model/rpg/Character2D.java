package br.odb.multiplayer.model.rpg;

import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Direction;
import br.odb.gameworld.Kind;
import br.odb.gameworld.Position;

public class Character2D extends CharacterActor {

	final Position position = new Position();
	Direction direction = Direction.N;
	
	public Character2D(String name, Kind kind) {
		super(name, kind);

	}

	
	
}
