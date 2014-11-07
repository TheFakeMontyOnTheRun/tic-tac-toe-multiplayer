package br.odb.knights;

import java.util.ArrayList;

import br.odb.utils.math.Vec2;


public class GameLevel extends Layer {

	private Tile[][] tileMap;
	ArrayList<Actor> entities;
	private ArrayList<Actor> dead;
	private int remainingKnights;
	int remainingMonsters;

	@Override
	public String toString() {

		String toReturn = "";

		for (Actor a : entities) {
			if (a.isAlive()) {

				toReturn += a.getStats();
			}
		}

		return toReturn;
	}

	public GameLevel() {
		super();

		entities = new ArrayList<Actor>();
		dead = new ArrayList<Actor>();
		tileMap = new Tile[getGameWidth()][getGameHeight()];
	}


	public GameLevel(int[][] map ) {
		remainingKnights = 3;
		tileMap = new Tile[getGameWidth()][getGameHeight()];
		entities = new ArrayList<Actor>();
		dead = new ArrayList<Actor>();
		int[] row;
		Tile tile;
		TileType[] types = TileType.values();
		for (int c = 0; c < map.length; ++c) {
			row = map[c];
			
			for (int d = 0; d < row.length; ++d) {

				tile = new Tile(c, d, types[ row[d] ] );

				switch ( TileType.values()[ row[d] ] ) {
				case BRICKS:
					tile.setBlock(true);
					break;
				case DOOR:
					tile.setBlock(false);
					break;
				case BEGIN:
					tile.setBlock(true);
					break;
				default:
					tile.setBlock(false);
				}
				this.add(tile);
				this.tileMap[c][d] = tile;
			}
		}
	}

	public void setDificulty(byte dificulty) {

	}

	public void tick() {
		Monster m;

		remainingMonsters = 0;
		for (Actor a : entities) {
			if (a instanceof Monster && a.isAlive()) {
				m = (Monster) a;
				m.updateTarget(this);
				++remainingMonsters;
			}
		}

		// for ( Actor a : dead ) {
		// if ( entities.contains( a ) ) {
		// entities.remove( a );
		// }
		// }
	}

	public void reset() {
		TileType kind;
		for (int c = 0; c < tileMap.length; ++c) {
			for (int d = 0; d < tileMap[c].length; ++d) {

				kind = tileMap[c][d].getKind();

				switch ( kind ) {

				case SPAWNPOINT_BAPHOMET:
					addEntity(new Baphomet(), c, d);
					++remainingMonsters;
					break;
				case SPAWNPOINT_BULL:
					addEntity(new BullKnight(), c, d);
					break;
				case SPAWNPOINT_TURTLE:
					addEntity(new TurleKnight(), c, d);
					break;
				case SPAWNPOINT_EAGLE:
					addEntity(new EagleKnight(), c, d);
					break;
				case SPAWNPOINT_CUCO:
					addEntity(new Cuco(), c, d);
					++remainingMonsters;
					break;
				case SPAWNPOINT_MOURA:
					addEntity(new Moura(), c, d);
					++remainingMonsters;
					break;
				case SPAWNPOINT_DEVIL:
					addEntity(new Demon(), c, d);
					++remainingMonsters;
					break;
				}
			}
		}
	}

	private Actor addEntity(Actor actor, int c, int d) {
		add(actor);
		entities.add(actor);
		tileMap[c][d].ocupant = (actor);
		actor.position.set(new Vec2(c, d));
		return actor;
	}

	public Tile getTile(Vec2 position) {
		return this.tileMap[(int) position.x][(int) position.y];
	}

	public int getTotalActors() {
		return entities.size();
	}

	public GameObject getActor(int c) {
		return entities.get(c);
	}

	public boolean validPositionFor(Actor actor) {

		int c, d;
		c = (int) actor.position.x;
		d = (int) actor.position.y;

		if (tileMap[c][d].isBlock())
			return false;

		if ((tileMap[c][d].ocupant instanceof Actor)
				&& !((Actor) tileMap[c][d].ocupant ).isAlive())
			return true;

		if ((tileMap[c][d].ocupant instanceof Knight)
				&& ((Knight) tileMap[c][d].ocupant ).hasExited)
			return true;

		if (tileMap[c][d].ocupant instanceof Actor)
			return false;

		return true;
	}

	public Actor getActorAt(int x, int y) {

		if (tileMap[x][y].ocupant instanceof Actor)
			return ((Actor) tileMap[x][y].ocupant );
		else
			return null;
	}

	public void battle(Actor attacker, Actor defendant) {

		Vec2 pos;

		attacker.attack(defendant);
		defendant.attack(attacker);

		if (!attacker.isAlive()) {

			pos = attacker.position;
			dead.add(attacker);
			tileMap[(int) pos.x][(int) pos.y].ocupant = (null);
		}

		if (!defendant.isAlive()) {

			pos = defendant.position;
			dead.add(defendant);
			tileMap[(int) pos.x][(int) pos.y].ocupant = (null);
		}
	}

	public int getGameWidth() {

		return 20;
	}

	public int getGameHeight() {

		return 20;
	}



	public int getAliveKnigts() {

		return remainingKnights;
	}

	public Actor getActorAt( Vec2 position) {

		return getActorAt((int) position.x, (int) position.y);
	}

	public Knight[] getKnights() {
		ArrayList<Knight> knights_filtered = new ArrayList<Knight>();

		for (Actor a : entities) {
			if (a instanceof Knight && a.isAlive() && !((Knight) a).hasExited) {
				knights_filtered.add((Knight) a);
			}
		}

		Knight[] knights = new Knight[knights_filtered.size()];
		return knights_filtered.toArray(knights);
	}

	public int getMonsters() {
		return remainingMonsters;
	}

	public Actor addEntity(Actor a, int c, int d, int h) {
		a.healthPoints = h;		
		return addEntity( a, c, d);
	}

	public void setOcupant(int x, int y, Actor a) {
		tileMap[ y ][ x ].ocupant = ( a );		
	}
}
