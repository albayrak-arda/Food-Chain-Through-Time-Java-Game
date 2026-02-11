package logic;

import helpers.RandomGenerator;

public class Food extends Organism {
	/**
	 * 
	 * @param name: name of the food
	 * @param x: constant(if it is not eaten) x coordinate of the food
	 * @param y: constant(if it is not eaten) y coordinate of the food
	 * @param era: era(enum) to which this food belongs to
	 * Food constructor that only calls its superclass (Organism)
	 */
	public Food(String name, int x, int y, Era era) {
		super(name,x,y,era);
	}
	/**
	 * 
	 * @param gridSize: the length of one side of the large square on which the game is played
	 * this method respawns the food when it is eaten by Apex Prey or Predator (only prey gain score)
	 */
	public void respawn(int gridSize) {
		this.x = RandomGenerator.getNextInt(gridSize);
		this.y = RandomGenerator.getNextInt(gridSize);
	}
	

}
