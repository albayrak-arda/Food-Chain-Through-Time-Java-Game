package logic;


import java.util.ArrayList;

import helpers.RandomGenerator;

/**
 * A concrete subclass of abstract superclass Animal that represents the Preys.
 */
public class Prey extends Animal {
	
	
	/**
	 * 
	 * @param name: name of the Prey
	 * @param x: x coordinate of the Prey Object 
	 * @param y: y coordinate of the Prey Object 
	 * @param era: the era that the prey object belongs to
	 */
	public Prey(String name, int x, int y, Era era) {
		super(name,x,y,getMaxCooldown(era),era);
		this.currentCoolDown = this.maxCoolDown;
		
	}
	public void resetCooldown() {
		this.currentCoolDown = this.maxCoolDown+1;
	}
	
	private static int getMaxCooldown(Era era) {
        switch (era) {
            case PAST:
                return 2;
            case PRESENT:
                return 3;
            case FUTURE:
                return 2; 
            default:
                return 2;
        }
    }
	/**
     * when the Prey use its special skill this method must be called.
     * @param: targetX: targeted x coordinate by using special skill move
     * @param: targetY: targeted y coordinate by using special skill move
     */
	public void specialSkill(int targetX, int targetY) {
		this.setX(targetX);
        this.setY(targetY);
        this.resetCooldown();
		
	}
	/**
	 * 
	 * @param range: indicates the size of the big square that will be checked by method to find 
     * best grid (range*2+1)
	 * @param predatorsAndApredators: list of enemies(hunters) of the Prey in the map
	 * @param foods: list of foods in the map
	 * @param gridSize: edge length of the square that is used in a game
	 * @param canEatFood:indicates whether the prey can eat food during that round or not.
	 * In the future era, Prey can use its special skill for escaping from its enemies but cannot eat
	 * food this turn, so canEatFood:true --> prey can eat food this turn
	 * (true for only except using special skill in the future era)
	 * @return: an array that contains x and y coordinates of the best grid which has the highest score
     * among the scanned grids and its total score.
     * the method compares all the unit squares in a large square with a edge length of range*2+1,
     *  centered on the square currently taken, according to a scoring logic,
     *   and finds the highest-scoring square.
	 */
	private double[] findBestMove(int range, ArrayList<Animal> predatorsAndApredators, ArrayList<Food> foods,
            int gridSize, boolean canEatFood) {

        int optimalX = this.getX();
        int optimalY = this.getY();
        double maxScore = -100000.0;

        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                
                if (i == 0 && j == 0) {
                	continue;
                }

                if (this.era == Era.PAST && range == 2) {// zigzag move(L)
                    if (Math.abs(i) + Math.abs(j) != 3) {
                        continue; 
                    }
                }
                
                else if (this.era == Era.PRESENT && range == 2) {
                    boolean isStraight2 = false;
                    if (Math.abs(i) == 2 && j == 0) {
                        isStraight2 = true;
                    } 
                    else if (Math.abs(j) == 2 && i == 0) {
                        isStraight2 = true;
                    }

                    boolean isDiagonal2 = false;
                    if (Math.abs(i) == 2 && Math.abs(j) == 2) {
                        isDiagonal2 = true;
                    }
                    
                    if (!isStraight2 && !isDiagonal2) {
                        continue;
                    }
                }

                int checkingX = this.getX() + i;
                int checkingY = this.getY() + j;

                if (checkingX >= 0 && checkingX < gridSize && checkingY < gridSize && checkingY >= 0) {
                    
                    boolean isEnemyExist = false;
                    for (Animal enemy : predatorsAndApredators) {
                        if (enemy.getX() == checkingX && enemy.getY() == checkingY) {
                            isEnemyExist = true;
                            break;
                        }
                    }
                    if (isEnemyExist) {
                        continue; 
                    }

                    double totalScore = 0.0;
                    
                    double minDistToEnemy = 10000.0;
                    for (Animal a : predatorsAndApredators) {
                        double distance = Math.abs(a.getX() - checkingX) + Math.abs(a.getY() - checkingY);
                        if (distance < minDistToEnemy) {
                            minDistToEnemy = distance;
                        }
                    }
                    totalScore += minDistToEnemy * 6; 

                    if (canEatFood && !foods.isEmpty()) {
                        double minDistToFood = 10000.0;
                        boolean isOnTheFood = false;
                        for (Food f : foods) {
                            double distance = Math.abs(f.getX() - checkingX) + Math.abs(f.getY() - checkingY);
                            if (distance < minDistToFood) {
                                minDistToFood = distance;
                            }
                            if (f.getX() == checkingX && f.getY() == checkingY) {
                                isOnTheFood = true;
                            }
                        }
                        totalScore -= minDistToFood * 5; 

                        if (isOnTheFood) {
                            totalScore += 100.0; 
                        }
                    }
                    
                    if (totalScore > maxScore) {
                        maxScore = totalScore;
                        optimalX = checkingX;
                        optimalY = checkingY;
                    } else if (totalScore == maxScore && RandomGenerator.getNextInt(2) == 1) {
                        optimalX = checkingX;
                        optimalY = checkingY;
                    }
                }
            }
        }
        double[] newArr ={ optimalX, optimalY, maxScore };
        return newArr;
    }

	/**
	 *this method moves the Prey object optimally(AI logic),it  uses findBestMove method 
	 *to  compare the scores of squares that can be reached using special abilities 
	 *and normal move actions and decide which is more optimal(higher score) and select it.
	 * @param predatorsAndApredators: list of hunters of Prey on the map
	 * @param foods: list of foods on the map
	 * @param gridSize: edge lentgh of the square that is used in a game
	 */
    public void move(ArrayList<Animal> predatorsAndApredators, ArrayList<Food> foods, int gridSize) {
        
        double[] optimalNormalMove = findBestMove(1, predatorsAndApredators, foods, gridSize, true);
        
        if (this.isSpecialSkillAvailable()) {
            double[] optimalSpecialMove = null;

            if (this.era == Era.PRESENT) {
                optimalSpecialMove = findBestMove(2, predatorsAndApredators, foods, gridSize, true);
            } 
            else if (this.era == Era.PAST) {
                optimalSpecialMove = findBestMove(2, predatorsAndApredators, foods, gridSize, true);
            } 
            else if (this.era == Era.FUTURE) {
                optimalSpecialMove = findBestMove(3, predatorsAndApredators, foods, gridSize, false);
            }

            if (optimalSpecialMove != null && optimalSpecialMove[2] > optimalNormalMove[2]) {
                specialSkill((int) optimalSpecialMove[0], (int) optimalSpecialMove[1]);
                return;
            }
        }
        
        basicMove((int) optimalNormalMove[0], (int) optimalNormalMove[1]);
    }
	 
	
	
	
	
	
	

}
