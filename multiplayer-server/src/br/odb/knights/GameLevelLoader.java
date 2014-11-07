package br.odb.knights;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GameLevelLoader {

	public static final byte LIMIT = 4;

	public static GameLevel loadLevel(byte currentLevel ) {
		int[][] map = null;
		InputStream in = null;

		try {
			in = new FileInputStream( "" + currentLevel );
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		DataInputStream dis = new DataInputStream(in);
		int buffer = 0;
		int lenX;
		int lenY;

		try {
			lenX = 20;
			lenY = 20;

			map = new int[lenY][lenX];

			for (int c = 0; c < lenX; ++c) {
				for (int d = 0; d < lenY; ++d) {
					buffer = dis.read();
					map[d][c] = buffer - '0';
				}
				in.skip(1); // pula o \n
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		GameLevel toReturn = new GameLevel(map);

		return toReturn;
	}

	public static GameLevel loadLevel(String data, GameLevel level ) {

		level.entities.clear();
		level.remainingMonsters = 0;
		String[] entries = data.split("\\|");
		String[] datum;
		int character;
		int c;
		int d;
		int h;
		Actor a;
		
		ArrayList< GameObject > toDelete = new ArrayList<GameObject>();
		
		for ( int x = 0; x < level.getGameWidth(); ++x ) {
			for ( int y = 0; y < level.getGameHeight(); ++y ) {
				level.setOcupant( x, y, null );
			}
		}
		
		for ( GameObject r : level.children ) {
			if ( r instanceof Actor ) {
				toDelete.add( r );
			}
		}
		

		level.children.removeAll( toDelete );

		
		for ( String entry : entries ) {
			
			datum = entry.split("\\,");
			
			character = Integer.parseInt( datum[ 0 ] );
			a = null;;
			c = Integer.parseInt( datum[ 1 ] );
			d = Integer.parseInt( datum[ 2 ] );
			h = Integer.parseInt( datum[ 3 ] );

			switch ( TileType.values()[ character ]) {
			case SPAWNPOINT_BAPHOMET:
				a = level.addEntity(new Baphomet(), c, d, h );
				++level.remainingMonsters;
				break;
			case SPAWNPOINT_BULL:
				a = level.addEntity(new BullKnight(), c, d, h );
				break;
			case SPAWNPOINT_TURTLE:
				a = level.addEntity(new TurleKnight(), c, d, h );
				break;
			case SPAWNPOINT_EAGLE:
				a = level.addEntity(new EagleKnight(), c, d, h );
				break;
			case SPAWNPOINT_CUCO:
				a = level.addEntity(new Cuco(), c, d, h );
				++level.remainingMonsters;
				break;
			case SPAWNPOINT_MOURA:
				a = level.addEntity(new Moura(), c, d, h );
				++level.remainingMonsters;
				break;
			case SPAWNPOINT_DEVIL:
				a = level.addEntity(new Demon(), c, d, h );
				++level.remainingMonsters;
				break;
			}
			
			if ( h <= 0 ) {
				a.kill();
			}			
		}
		


		

		return level;
	}
}
