package logic;



public class Predator extends Animal{
	
	/**
	 * Constructor of the Concrete class Predator that calls the constructor of the Animal Class( Also
	 * Animal class calls the Organism Class's constructor in its constructor)
	 * @param name: name of the Predator
	 * @param x: x coordinate of the Predator object.
	 * @param y: y coordinate of the Predator object.
	 * @param maxCoolDown: indicates the waiting time to use special skill again for Predator.
	 * @param era: indicates the that the predator belongs to
	 */
	public Predator(String name, int x, int y, Era era) {
		super(name,x,y,getMaxCooldown(era),era);
	
	}
	private static int getMaxCooldown(Era era) {
        switch (era) {
            case PAST:
                return 2;
            case PRESENT:
                return 0;
            case FUTURE:
                return 2; 
            default:
                return 2;
        }
    }
	
	@Override
	/**this method must be called when the predator use common walk abilty in a game
	 * @param: targetX:target location's x coordinate after the predator use
	 *  common walk ability(surrounding 8 squares)
	 * @param: targetY:target location's x coordinate after the predator use 
	 * common walk ability(surrounding 8 squares)
	 */
	public void basicMove(int targetX, int targetY) {
		int differenceX = Math.abs(targetX - this.getX());
		int differenceY = Math.abs(targetY - this.getY());
		
		if((differenceX >1|| differenceY >1)) {
			System.out.println("Invalid for common walk move.");// if range > 1 --> special Skill
			return;
		}
		super.basicMove(targetX,targetY);
		System.out.println("Predator use common walk move.");
	}
	/**
	 * this method executes the special move if the conditions are satisfied, also use canSpecialMoveTo
	 * method to check if it is possible the use special skill or not
	 * @param targetX: targeted location's x coordinate after Special Skill move executed
	 * @param targetY: targeted location's y coordinate after Special Skill move executed
	 * @param gridSize: edge length of the square that is used in game as a grid(map)
	 * @param isApexAdjacent: indicates if predator (player) is adjacent to apex or not
	 * @return: true if Special skill is available and may be used at this moment, return false if not
	 */
	public boolean specialMove(int targetX, int targetY, int gridSize, boolean isApexAdjacent) {
		if (!canSpecialMoveTo(targetX, targetY, gridSize, isApexAdjacent)) {
			return false;
		}
		else {
			this.setX(targetX);
		    this.setY(targetY);
		    if(this.era != Era.PRESENT) {// there is no cooldown in present era (adjacent to apex or not)
		        this.resetCooldown();
		    }
		    System.out.println("Special move executed!");
		    return true;
		}
	}
	/**
	 * this method controlls the special move is possible or not. It does not execute any move.
	 * @param targetX: checked location's x coordinate (checking for special skill move is possible or not)
	 * @param targetY: checked location's y coordinate (checking for special skill move is possible or not)
	 * @param gridSize: edge length of the square that is used in game as a grid(map)
	 * @param isApexAdjacent: indicates if predator (player) is adjacent to apex or not
	 * @return: true if Special move is possible, false if not
	 */
	public boolean canSpecialMoveTo(int targetX, int targetY, int gridSize, boolean isApexAdjacent) {
	    if(targetX < 0 || targetX >= gridSize || targetY < 0 || targetY >= gridSize) {
	    	return false;
	    }

	    int diffX = Math.abs(targetX - this.getX());
	    int diffY = Math.abs(targetY - this.getY());
	    int distance = Math.max(diffX, diffY);
	    if (distance != 2) {
	    	return false;
	    }

	    if (this.getEra() == Era.PRESENT) {
	        if (!isApexAdjacent) {
	        	return false;
	        }
	    }
	    else {
	        if (this.isSpecialSkillAvailable() == false) {
	        	return false;
	        }
	    }

	    if (this.getEra() == Era.PAST ) {
	    	if (diffX != 0 && diffY != 0) {
	    		return false;
	    	}
	    }
	    else if(this.getEra() == Era.FUTURE) {
	    	if (diffX != 0 && diffY != 0 && diffX != diffY) {
	    		return false;
	    	}
	    }

	    return true;
	}
	
	
}
