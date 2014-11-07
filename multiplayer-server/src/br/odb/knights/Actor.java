package br.odb.knights;

import br.odb.utils.math.Vec2;


public abstract class Actor extends GameObject {

	public static enum Actions{ MOVE_UP, MOVE_RIGHT, MOVE_DOWN, MOVE_LEFT };
	
	int healthPoints;
	private int attackPoints;
	protected final Vec2 previousPosition = new Vec2();
	
	public void attack(Actor actor) {
		
		this.healthPoints -= actor.attackPoints;

		if ( healthPoints <= 0 ) {
			kill();
		}
	}
	
	public void kill()  {
	}
	
	public boolean isAlive() {
		return ( healthPoints > 0 );
	}
	
	public Actor( int healthPonts, int attackPoints ) {
		super();
		this.healthPoints = healthPonts;
		this.attackPoints = attackPoints;
	}

	public void act(Actions action ) {
		
		switch ( action ) {
			case MOVE_UP:
				this.position.addTo( new Vec2( 0, -1 ) );
			break;
			
			case MOVE_DOWN:
				this.position.addTo( new Vec2( 0, 1 ) );
			break;
			
			case MOVE_LEFT:
				this.position.addTo( new Vec2( -1, 0 ) );
			break;
			
			case MOVE_RIGHT:
				this.position.addTo( new Vec2( 1, 0 ) );
			break;
			
		}
	}


	public void checkpointPosition() {
		previousPosition.set( position );
		
	}

	public void undoMove() {
		position.set( previousPosition );
	}

	public abstract String getChar();

	public String getStats() {
		return getChar() + "," + ((int)position.x) + "," + ((int)position.y) + "," + healthPoints + "|";
	}}
