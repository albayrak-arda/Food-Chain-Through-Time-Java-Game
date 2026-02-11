package logic;
/**
 * An abstract class that represents the only moveable organisms
 * (food class does not extend this class)
 */
public abstract class Animal extends Organism {
	
	protected int score;
	protected final int maxCoolDown;
	protected int currentCoolDown; // indicates current remaining time to use special skill
	
	/**
	 * 
	 * @param name: name of the Animal
	 * @param x: current x coordinate of the Animal
	 * @param y: current y coordinate of the Animal
	 * @param maxCoolDown: represents the time needed to use special skill(move)
	 */
	public Animal(String name , int x, int y , int maxCoolDown, Era era) {
		super(name,x,y,era);
		this.score = 0;
		this.currentCoolDown = 0;
		this.maxCoolDown = maxCoolDown;
		
		
		
	}
	/**
	 * 
	 * @param nextX: represents the new x coordinate after move method executed.
	 * @param nextY: represents the new y coordinate after move method executed.
	 */
	public void basicMove(int nextX, int nextY) {
		this.setX(nextX);
		this.setY(nextY);
	}
	/**
	 * 
	 * @param targetX: new x coordinate after the animal used its special skill move
	 * @param targetY: new y coordinate after the animal used its special skill move.
	 */
	
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 *
	 * @param value: represents the current time needed to use special skill
	 */
	public void setCurrentCoolDown(int value) {
		this.currentCoolDown = value;
	}
	/**
	 * 
	 * @return the currentCoolDown: we use this method when we need to know what is
	 *  the remaining time for using the special skill of Animal.
	 */
	public int getCurrentCoolDown() {
		return currentCoolDown;
	}
	/**
	 * 
	 * @return score: we will use this method when printing the scores of players.
	 */
	public int getScore() {
		return score;
	} 
	/**
	 * After completing each round, we decrease the currentCoolDown by 1 
	 * by using this method(ex: 3->2)
	 */
	public void decrementCoolDownEachRound() {
		if(this.currentCoolDown > 0) {
			this.currentCoolDown--;
		}
	}
	/**
	 * reset cooldown to maxCoolDown after using special skill
	 */
	public void resetCooldown() {
		this.currentCoolDown = this.maxCoolDown+1;  
	}
	public boolean isSpecialSkillAvailable() {
		if(this.currentCoolDown == 0) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param point: after the player win or lose points in a round, we update 
	 * the total score of player(score can be positive or negative integer(+3 +1,-1)
	 */
	public void changeScore(int point) {
		this.score += point;
	}
	/**
	 * 
	 * @return era
	 */
	public Era getEra() {
		return era;
	}
	/**
	 * 
	 * @param newX: x coordinate of Animal after respawning
	 * @param newY: y coordinate of Animal after respawning 
	 */
	public void respawnAfterLose(int newX ,int newY) {
		this.setX(newX);
		this.setY(newY);
		this.changeScore(-1); // since it lost the round which is just before the respawning
	}
	
	
		
	

}
