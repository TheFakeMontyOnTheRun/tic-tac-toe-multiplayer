/**
 * 
 */
package br.odb.knights;


/**
 * @author monty
 *
 */
public abstract class Monster extends Actor 
{
	public Monster( int healthPonts, int attackPoints ) {
		super( healthPonts, attackPoints );
	}


	void updateTarget( GameLevel level ) {
		
	}

	
	boolean dealWith( Knight knight, GameLevel level, int relX, int relY ) {
		
		boolean moved = false;
		
		checkpointPosition();

		if (relY > 0) {
			act(Actions.MOVE_DOWN);
			moved = true;
		} else if (relY < 0) {
			act(Actions.MOVE_UP);
			moved = true;
		} else if (relX > 0) {
			act(Actions.MOVE_RIGHT);
			moved = true;
		} else if (relX < 0) {
			act(Actions.MOVE_LEFT);
			moved = true;
		}

		if (moved) {

			Tile loco = level.getTile( position );

			if (!level.validPositionFor(this)) {

				if (!isAlive()) {
					loco.ocupant = (this);
					return false;
				} else if ( loco.ocupant instanceof Knight ) {
					Knight k = (Knight) loco.ocupant;
					if ( !k.hasExited && k.isAlive() ) {
						k.attack( this );
					}
				}
				this.undoMove();
			} else {
				loco = level.getTile( previousPosition );
				loco.ocupant = (null);
				loco = level.getTile( position );
				loco.ocupant = (this);
			}

		}
		
		return moved;
	}
}
