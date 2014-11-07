package br.odb.knights;

public abstract class Knight extends Actor {
	
	public boolean hasExited;

	public Knight(  int healthPoints, int attackPoints ) {
		super( healthPoints, attackPoints );
	}
	
	public int getStamina() {

		return 2;
	}
	
	@Override
	public String toString() {
	
		return super.healthPoints + " HP";
	}

	public void setAsExited() {
		hasExited = true;		
	}
}
