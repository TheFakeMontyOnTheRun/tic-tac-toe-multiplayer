/**
 * 
 */
package br.odb.knights;

import br.odb.utils.math.Vec2;

/**
 * @author monty
 * 
 */
public class Moura extends Monster {

	/**
	 * @param resId
	 */
	public Moura() {
		super( 8, 3 );
	}

	@Override
	public void updateTarget(GameLevel level) {
		Vec2 myPosition = position;
		Vec2 scan = new Vec2();
		int newX;
		int newY;

		for (int x = -2; x < 2; ++x) {
			for (int y = -2; y < 2; ++y) {

				newX = (int) (x + myPosition.x);
				newY = (int) (y + myPosition.y);
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
		return String.valueOf( TileType.SPAWNPOINT_MOURA );
	}
}
