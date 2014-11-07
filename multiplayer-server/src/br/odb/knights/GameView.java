/**
 * 
 */
package br.odb.knights;

import br.odb.knights.Actor.Actions;
import br.odb.utils.Updatable;

/**
 * @author monty
 * 
 */
public class GameView {



	private GameSession gameSession;
	public GameLevel currentLevel;
	private int aliveKnightsInCurrentLevel;
	public boolean playing = false;
	private Updatable gameDelegate;
	public int exitedKnights;

	public void init(Updatable updateDelegate, byte level) {


		aliveKnightsInCurrentLevel = 3;
		this.gameSession = GameConfigurations.getInstance()
				.getCurrentGameSession();
		this.gameDelegate = updateDelegate;
	
	}


	public void tick( Actor selectedPlayer, Actions action ) {

		if (selectedPlayer == null)
			return;

		if (!selectedPlayer.isAlive()) {
			selectedPlayer = null;
			gameDelegate.update( 0 );
			return;
		}

		boolean moved = false;

		Tile loco = currentLevel.getTile(selectedPlayer.position);

		selectedPlayer.checkpointPosition();

	
		selectedPlayer.act( action );
		

		if (!this.currentLevel.validPositionFor(selectedPlayer)) {

			if (currentLevel.getActorAt(selectedPlayer.position) != null
					&& !(currentLevel.getActorAt(selectedPlayer.position) instanceof Knight)) {
				currentLevel.battle(selectedPlayer,
						currentLevel.getActorAt(selectedPlayer.position));
			}

			if (!selectedPlayer.isAlive()) {
				selectedPlayerHasDied();
				gameDelegate.update( 0 );
				return;
			}
			selectedPlayer.undoMove();
		} else {
			loco.ocupant = (null);
			loco = currentLevel.getTile(selectedPlayer.position);
			loco.ocupant = (selectedPlayer);
		}

		if (moved) {

			currentLevel.tick();
		}

		if (!selectedPlayer.isAlive()) {
			selectedPlayerHasDied();
		}

		if (loco.getKind() == TileType.DOOR) {

			if ( ( aliveKnightsInCurrentLevel - exitedKnights ) > 1 ) {
				
//				Toast.makeText( this.getContext(), "Your knight sucessfully exited the door", Toast.LENGTH_SHORT ).show();
			}
			
			((Knight) selectedPlayer).setAsExited();
			++exitedKnights;
		}

		gameDelegate.update( 0 );
	}

	public void selectedPlayerHasDied() {

		aliveKnightsInCurrentLevel--;

//		if (aliveKnightsInCurrentLevel == 0) {
//			
//			Intent intent = new Intent();
//			intent.putExtra( "good", 2 );
//			GameActivity activity = ((GameActivity) this.getContext());
//			activity.setResult( Activity.RESULT_OK, intent );
//			activity.finish();
//		} else {
//			Toast.makeText(getContext(), "Your knight is dead!",
//					Toast.LENGTH_SHORT).show();
//			selectedPlayer = null;
//		}
	}






}
