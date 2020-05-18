package br.odb.multiplayer.model.rpg;

import java.util.Arrays;

import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Direction;
import br.odb.gameworld.Location;

public class Room2D extends Location {

	char[][] map;
	Character2D[][] characters;
	
	public Room2D(String name, String data) {
		super(name);
		
		int length = data.indexOf('\n') + 1;
		int count = (int) data.chars().filter(ch -> ch == '\n').count();
		
		map = new char[count][];
		characters = new Character2D[count][];
		
		int position = 0;
		for ( int c = 0; c < count; ++c ) {
			map[c] = new char[length];
			characters[c] = new Character2D[length];
			
			for ( int d = 0; d < length; ++d ) {
				map[c][d] = data.charAt(position++);
			}
		}
	}
	
	

	@Override
	public void addCharacter(CharacterActor character) {
		// TODO Auto-generated method stub
		super.addCharacter(character);
		
		Character2D c2d = (Character2D)character;
		
		if (c2d != null) {
			c2d.position.y = map.length / 2;
			c2d.position.x = map[0].length / 2; 
		}
	}



	@Override
	public String toString() {

		for ( int c = 0; c < map.length; ++c ) {
			for ( int d = 0; d < map[c].length; ++d ) {
				characters[c][d] = null;
			}
		}
		
		for ( CharacterActor ca : getCharacters() ) {
		
			if (ca instanceof Character2D) {
				Character2D c2d = (Character2D)ca;
				characters[c2d.position.y][c2d.position.x] = c2d;
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString() + '\n');
		
		for ( int c = 0; c < map.length; ++c ) {
			for ( int d = 0; d < map[c].length; ++d ) {
				
				if (characters[c][d] != null ) {
					sb.append(Direction.getDirectionIndicator(characters[c][d].direction));
				} else {
					sb.append(map[c][d]);	
				}
			}
		}
		
		return sb.toString();
	}
	
	
}
