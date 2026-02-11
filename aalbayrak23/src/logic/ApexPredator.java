package logic;


import java.util.ArrayList;
import helpers.RandomGenerator;

public class ApexPredator extends Animal {

    public ApexPredator(String name, int x, int y, Era era) {
        super(name, x, y, getMaxCooldown(era), era);
    }
    /**
     * 
     * @param era: this apex's era
     * @return: maxCoolDown value for apex according to era types
     */
    private static int getMaxCooldown(Era era) {
        switch (era) {
            case PAST:
                return 2;
            case PRESENT:
                return 3;
            case FUTURE:
                return 3; 
            default:
                return 2;
        }
    }
    
    /**
     * when the ApexPredator select use its special skill this method must be called.
     * @param: targetX: targeted x coordinate by using special skill move
     * @param: targetY: targeted y coordinate by using special skill move
     */
    public void specialSkill(int targetX, int targetY) {
    	this.setX(targetX);
    	this.setY(targetY);
    	this.resetCooldown(); // since it used its special skill, cooldown must be reseted.
    }



    /**
     * 
     * @param preysAndPredators: list of the enemies of the Apex Predator.
     * @param gridSize: length of an edge of the square which is used while playing the game
     * this method decides if special skill have to be used or  normal move have to be used
     * by comparing their scores(findBestGrid method used for calculating their scores)
     *  and the current availability of the special skill of apex.
     * Also, after it decides which move is more optimal, it implements the move
     */
    public void catchTarget(ArrayList<Animal> preysAndPredators, int gridSize) {
        
        
        double[] optimalNormalMove = findBestGrid(1, preysAndPredators, gridSize, false); 
        // this line find the best possible normal walk option(1 of the 8 surrounding squares)
        
        double[] optimalSpecialMove = null;

        if (this.isSpecialSkillAvailable() == true) {
            
            if (this.era == Era.PAST) {
                optimalSpecialMove = findBestGrid(2, preysAndPredators, gridSize, true);
            } 
            else if (this.era == Era.PRESENT) {
                optimalSpecialMove = findBestGrid(3, preysAndPredators, gridSize, true);
            } 
            else if (this.era == Era.FUTURE) {
                optimalSpecialMove = findBestGrid(3, preysAndPredators, gridSize, false);
            }
        }

        
        if (optimalSpecialMove != null && optimalSpecialMove[2] > optimalNormalMove[2]) {
            specialSkill((int)optimalSpecialMove[0], (int)optimalSpecialMove[1]);// move by special skill
        } 
        else {
            basicMove((int)optimalNormalMove[0], (int)optimalNormalMove[1]);// move by common basic move ability
        }
    }

    /**
     * 
     * @param range: indicates the size of the big square that will be checked by method to find 
     * best grid (range*2+1)
     * @param preysAndPredators: enemies of apex (targets of apex)
     * @param gridSize: size of the square which is used while playing this game.
     * @param straightOnly: It depicts motion in a straight line without any rotation
     *  in 8 different directions
     * @return: an array that contains x and y coordinates of the best grid which has the highest score
     * among the scanned grids and its total score.
     * the method compares all the unit squares in a large square with a edge length of range*2+1,
     *  centered on the square currently taken, according to a scoring logic,
     *   and finds the highest-scoring square.
     */
    private double[] findBestGrid(int range, ArrayList<Animal> preysAndPredators, int gridSize, boolean straightOnly) {
        
        int currentX = this.getX();
        int currentY = this.getY();
        
        int optimalX = currentX;
        int optimalY = currentY;
        
        double maxScore = -10000.0;  
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
            	if (i == 0 && j == 0) {
            		continue;
            	}

                boolean isHorizontal = false;// to be straight-only movement have to satisfy at least one of these(horizontal vertical or diagonal)
                boolean isVertical = false;
                boolean isDiagonal = false;
                if (straightOnly) {
                	if(j == 0) {
                		 isHorizontal = true;
                	}
                    if(i == 0) {
                         isVertical = true; 
                    }
                    if(Math.abs(i) == Math.abs(j)) {
                    	 isDiagonal = true;
                    }
                   
                    
                    if (isHorizontal == false && isVertical == false && isDiagonal == false) {
                        continue;
                    }
                    int movingDistance = Math.max(Math.abs(i),Math.abs(j));
                    
                    if(this.era == Era.PAST && movingDistance != range) {
                    	continue;//in the past era, range have to be 2.
                    }
                }

                int checkingX = currentX + i;//checking grid's x and y coordinates
                int checkingY = currentY + j;

                
                if (checkingX >= 0 && checkingX < gridSize && checkingY >= 0 && checkingY < gridSize) {
                	// validate if the checking grid option is inside of the square map
                    double minDistance = 10000.0;
                    
                    for (Animal p : preysAndPredators) {
                        double distance = Math.abs(p.getX() - checkingX) + Math.abs(p.getY() - checkingY);
                        if (distance < minDistance) {
                            minDistance = distance;
                        }
                    }
                    
                    double score = 500.0 - minDistance;

                    if (score > maxScore) {
                        maxScore = score;
                        optimalX = checkingX;
                        optimalY = checkingY;
                    } 
                    else if (score == maxScore && RandomGenerator.getNextInt(2) == 1) {
                    	// if there are 2 optimal options with the highest scores, this block randomly 
                    	//choose one of them with the possibility of 50%
                        optimalX = checkingX; 
                        optimalY = checkingY;
                    }
                }
            }
        }
        double[] newArr = {optimalX, optimalY, maxScore};
        return newArr;
    }
   
     
}
