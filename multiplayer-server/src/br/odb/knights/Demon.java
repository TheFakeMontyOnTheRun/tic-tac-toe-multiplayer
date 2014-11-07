/**
 * 
 */
package br.odb.knights;

import br.odb.utils.math.Vec2;

/**
 * @author monty
 *
 */
public class Demon extends Monster {

	/**
	 * @param resId
	 */
	public Demon() {
		super( 10, 2 );
	}

	

	@Override
	public void updateTarget(GameLevel level) {
		Vec2 scan = new Vec2();
		int newX;
		int newY;

		for (int x = -10; x < 10; ++x) {
			for (int y = -10; y < 10; ++y) {

				newX = (int) (x + position.x);
				newY = (int) (y + position.y);
				scan.x = newX;
				scan.y = newY;

				if (newX >= 0 && newY >= 0 && newX < level.getGameWidth()
						&& newY < level.getGameHeight()
						&& level.getTile(scan).ocupant instanceof Knight) {

					if (dealWith(
							((Knight) level.getTile(new Vec2(newX, newY))
									.ocupant), level, x, y))
						return;
				}
			}
		}		
	}



	@Override
	public String getChar() {
		return String.valueOf( TileType.SPAWNPOINT_DEVIL );
	}
}
