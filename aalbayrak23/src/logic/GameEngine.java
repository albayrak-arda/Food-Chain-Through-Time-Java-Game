package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import exceptions.LoadGameException;
import gui.GamePanel;
import gui.ResultFrame;
import helpers.RandomGenerator;
import fileIO.Logger;

public class GameEngine {
	
	private int gridSize;
	private Era era;
	private int totalRound;
	private int currentRound;
	private Organism[][] mapGrid;
	private ArrayList<Animal> animals;
	private Predator player;
	private int preyScore =0;
	private int predatorScore = 0;
	private int apexPScore = 0;
	private boolean isGameFinished = false;
	private GamePanel gamePanel;
	
	/**
	 * this method allows us to assign/modify an object to the gamePanel reference.
	 * @param gamePanel: GamePanel object that is instance variable of GameEngine class
	 */
	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	/**
	 * when Prey's score is needed, this method have to be used to access
	 * @return: Prey's score
	 */
	public int getPreyScore() {
		return preyScore;
	}

	/**
	 * when Predator's score is needed, this method have to be used to access
	 * @return: Predator's score
	 */
	public int getPredatorScore() {
		return predatorScore;
	}

	/**
	 * when ApexPredator's score is needed, this method have to be used to access
	 * @return: ApexPredator's score
	 */
	public int getApexPScore() {
		return apexPScore;
	}

	/**
	 * this is constructor method where we create the gameEngine object that
	 *  allows the game to start with specific features.
	 * @param gridSize: an edge length of the square that is used in a game.
	 * @param totalRound: total number of rounds the game will last 
	 * @param era: the era in which the game will be played
	 */
	public GameEngine(int gridSize, int totalRound, Era era) {
		this.gridSize = gridSize;
		this.totalRound = totalRound;
		this.era = era;
		this.mapGrid = new Organism[this.gridSize][this.gridSize];
		this.animals = new ArrayList<Animal>();
		this.currentRound =0;
		
		
	}
	
	/**
	 * This method is used when adding animals to the grid.
	 * @param animal: Animal object that will be added to Grid.
	 */
	private void addAnimalToGrid(Animal animal) {
		int animalX = animal.getX();
		int animalY = animal.getY();
		
		if(animalX >= 0 && animalX < gridSize && animalY >= 0 && animalY < gridSize) {
			animals.add(animal);
			mapGrid[animalX][animalY] = animal;
		}
		else {
			System.out.println("Outside of the method!, animal could not be added to grid");
		}
		
	}
	/**
	 * this method starts the game, firstly cleans the animals list, take the names of animals from the 
	 * foodchain.txt by invoking the method getNamesFromFoodChain, creates the animals with these names,
	 * creates the food and adds all organisms to map also add all animals to animal list.
	 * 
	 */
	public void startGame() {
		Logger.logGeneral("=== NEW GAME STARTED (Era: " + this.era + ") ===");
		if(gamePanel != null) {
			gamePanel.updateBackground(this.era); 
		}
		
		animals.clear();
		for(int i = 0; i< this.gridSize;i++) {
			for(int j = 0; j<this.gridSize ; j++) {
				mapGrid[i][j] = null;
			}
		}

		String[] selectedSetNames = getNamesFromFoodChain();
		String apexName = selectedSetNames[0];
		String predatorName = selectedSetNames[1];
		String preyName = selectedSetNames[2];
		String foodName = selectedSetNames[3];
		
		int px = RandomGenerator.getNextInt(gridSize);
		int py = RandomGenerator.getNextInt(gridSize);
		Predator myPlayer = new Predator(predatorName, px, py, era);
		this.player = myPlayer;
		addAnimalToGrid(myPlayer);
		
		int ax;
		int ay;
		do{
			ax = RandomGenerator.getNextInt(gridSize);
			ay = RandomGenerator.getNextInt(gridSize);
		}
		while (mapGrid[ax][ay] != null);
		ApexPredator apex = new ApexPredator(apexName, ax, ay, era);
		addAnimalToGrid(apex);
		
		int prx;
		int pry;
		do {
			prx = RandomGenerator.getNextInt(gridSize);
			pry = RandomGenerator.getNextInt(gridSize);
		}
		while (mapGrid[prx][pry] != null);
		Prey prey = new Prey(preyName, prx, pry, era);
		addAnimalToGrid(prey);
		
		int fx;
		int fy;
		do {
			fx = RandomGenerator.getNextInt(gridSize);
			fy = RandomGenerator.getNextInt(gridSize);
		}
		while (mapGrid[fx][fy] != null);
		Food food = new Food(foodName, fx, fy, era);
		mapGrid[fx][fy] = food;
		
		System.out.println("Game Initialized with set: " + apexName + " - " + predatorName + " - " + preyName);
		
		if(gamePanel != null) {
			gamePanel.updateBackground(this.era);
			gamePanel.repaint();
		}
	}
	/**
	 * 
	 * @return: the era of the GameEngine object which represents the era that game will last.
	 */
	public Era getEra() {
		return era;
	}

	/**
	 * this method returns organism specified by x and y coordinate in the map 
	 * @param x: x-coordinate of the organism object to be obtained
	 * @param y: y-coordinate of the organism object to be obtained
	 * @return: organism object that is wanted to obtain
	 */
	public Organism getOrganismAtGrid(int x , int y) {
		Organism organism = null;
		if(x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
			 organism = mapGrid[x][y];
		}
		return organism;
	}

	/**
	 * this method returns the gridsize of the big square(the board used in a game)
	 * @return: gridsize(an edge length of the square that is used in a game)
	 */
	public int getGridSize() {
		return gridSize;
	}

	/**
	 * 
	 * @return the total number of rounds of the game(instance v of gameEngine object)
	 */
	public int getTotalRound() {
		return totalRound;
	}

	/**
	 * 
	 * @return: the current round number in the game(e.g. 6/10)
	 */
	public int getCurrentRound() {
		return currentRound;
	}

	/**
	 * this method updates the grid after animal moves, clear the reference from the old location,
	 *  check where the animal move into the food or not, assign the new coordinates of animal after move.
	 * @param a: Animal object that moved and its coordinates will be updated with this method
	 * @param pastX: old x coordinate of a
	 * @param pastY: old y coordinate of a
	 */
	private void updateGridState(Animal a, int pastX, int pastY) {
	    if (mapGrid[pastX][pastY] == a) {
	    	// remove the reference from the old location of a 
	        mapGrid[pastX][pastY] = null;
	    }
	    int newX = a.getX();
	    int newY = a.getY();
	    if (newX >= 0 && newX < gridSize && newY >= 0 && newY < gridSize) {
	        if (mapGrid[newX][newY] instanceof Food) {//if a walked on a Food object
	            Food f = (Food) mapGrid[newX][newY];
	            if (a instanceof Prey) { // if a is a Prey
	            	Logger.log(this.currentRound, "EVENT: Prey ATE FOOD at (" 
	            + newX + "," + newY + ") | New Prey Score: " + this.preyScore);
	                this.preyScore += 3;
	             
	                System.out.println("LOGGED: Prey ate food.");
	            }
	            respawnFood(f); // since it is eaten by animal a
	        }
	        mapGrid[newX][newY] = a; // a's new location
	    }
	}
	/**
	 * this method moves the player after player clicks the valid square, also moves the prey before the
	 * moves the predator and finally moves the apexpredator
	 * @param targetX: the x coordinate where the player is to be taken by invoke this method
	 * @param targetY: the y coordinate where the player is to be taken by invoke this method
	 * @return true if player moved and also other animals moved and grid state updated, 
	 * false if nothing change(e.g game may already be finished or player may clicked invalid square
	 */
	public boolean movePlayer(int targetX, int targetY) {
	    if (isGameFinished) {
	        return false;
	    }
	    
	    moveAllPrey();// they have the first degree priority
	    
	    for(Animal a : animals) {
	        if(a instanceof Prey) {
	            resolveCollision(a);
	        }
	    }
	    int pastX = player.getX();
	    int pastY = player.getY();
	    int distance = Math.max(Math.abs(targetX - pastX), Math.abs(targetY - pastY));
	    boolean isPlayerMoved = false;
	    
	    if (distance == 0) {
	        System.out.println("Player stays this turn.");
	        isPlayerMoved = true; 
	    }
	    else if (distance == 1) { // basic move skill
	        player.basicMove(targetX, targetY);
	        isPlayerMoved = true;
	        Logger.log(this.currentRound, "Player used basic move.");
	        
	    } 
	    else if (distance >= 2) { // special skill
	        boolean isApexNear = checkApexAdjacent(); 
	        isPlayerMoved = player.specialMove(targetX, targetY, gridSize, isApexNear);
	        if (isPlayerMoved) {
	        	Logger.log(this.currentRound, "ACTION: Hunter used SPECIAL SKILL! (Era: " + era + ")");
	            
	        } 
	        else {
	        	Logger.log(this.currentRound, "WARNING: Special Move FAILED (Cooldown or Invalid)!");
	            
	        }
	    }
	    if (!isPlayerMoved) {
	        System.out.println("Invalid move!, click to valid square");
	        return false;
	    }
	    
	    updateGridState(player, pastX, pastY);
	    Logger.log(this.currentRound, "PLAYER: Predator moved from (" + pastX + "," + pastY +
	    		") to (" + player.getX() + "," + player.getY() + ")");
	    
	  
	    resolveCollision(player);
	    
	    moveAllApex();// they have least degree priority
	    
	    for(Animal a : animals) {
	        if(a instanceof ApexPredator) {
	            resolveCollision(a);
	        }
	    }
	    this.currentRound++; 
	    for(Animal a: animals) {
	        a.decrementCoolDownEachRound();
	    }
	    
	    if (this.currentRound >= this.totalRound || isGameFinished) {
	        isGameFinished = true;
	        String winner = "TIE";
	        int maxScore = Math.max(predatorScore, Math.max(preyScore, apexPScore));
	        boolean ifPredatorWins;
	        boolean ifPreyWins;
	        boolean ifApexWins;
	        
	        if(maxScore == predatorScore) {
	            ifPredatorWins = true;
	        }
	        else {
	            ifPredatorWins = false;
	        }
	        
	        if(maxScore == preyScore) {
	            ifPreyWins = true;
	        }
	        else {
	        	 ifPreyWins = false;
	        }
	        if(maxScore == apexPScore) {
	        	ifApexWins = true;
	        }
	        else {
	        	 ifApexWins = false;
	        }
	        
	        if(ifPredatorWins && !ifPreyWins && !ifApexWins) {
	        	winner = "PREDATOR";
	        }
	        else if(ifPreyWins && !ifPredatorWins && !ifApexWins) {
	        	winner = "PREY";
	        }
	        else if(ifApexWins && !ifPreyWins && !ifPredatorWins) {
	        	winner = "APEX PREDATOR";
	        }
	        String predatorName = this.player.getName();
	        String preyName = this.getCurrentPreyName(); 
	        String apexName = this.getCurrentApexName();
	        Logger.logGeneral("GAME OVER: " + winner + " WINS! Final Score: " + predatorScore);
	        ResultFrame resultScreen = new ResultFrame(this.predatorScore, this.preyScore, this.apexPScore, 
	            this.era, this.totalRound,predatorName,preyName, apexName);
	        resultScreen.setVisible(true);
	        
	        return false; 
	    }

	    return true; 
	}
	
	/**
	 * 
	 * @return: animals list that includes the animals which are in the map now
	 */
	public ArrayList<Animal> getAnimals() {
		return animals;
	}

	/**
	 * this method scans the 8 square that are surrounds the player and decide if apex adjacent(it is in
	 * any of these 8 squares ) or not
	 * @return: true if apex adjacent to player(predator), false if not
	 */
	public boolean checkApexAdjacent() {
		int playerX = player.getX();
		int playerY = player.getY();
		
		for( int i = -1; i<= 1; i++) {
			for(int j = -1; j<=1; j++) {
				if(i==j && i ==0) {
					continue;
				}
				int apexPosX = playerX +i;
				int apexPosY = playerY + j;
				if(apexPosX >= 0 && apexPosX<gridSize && apexPosY >= 0 && apexPosY < gridSize) {
					Organism o = mapGrid[apexPosX][apexPosY];
					if( o instanceof ApexPredator) {
						System.out.println("Apex was found");
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * after any of the animal was eaten, they are respawned to anywhere on the grid by
	 * using this method
	 * @param a: animal a that was eaten and will be respawned the grid
	 */
	private void respawnAnimal(Animal a) {
		if (mapGrid[a.getX()][a.getY()] == a) {
	        mapGrid[a.getX()][a.getY()] = null;
	    }
	    int newX, newY;
	    do {
	    	newX = RandomGenerator.getNextInt(gridSize);
	        newY = RandomGenerator.getNextInt(gridSize);
	    } 
	    while (mapGrid[newX][newY] != null);

	    a.setX(newX);
	    a.setY(newY);
	    mapGrid[newX][newY] = a;
	    System.out.println(a.getName() + " was eaten and respawned its new place " + newX + ", " + newY);
	}
	
	/**
	 * 
	 * @param f: the food f which was eaten by any of the animals and it will be respawned to grid by
	 * invoking this method
	 */
	private void respawnFood(Food f) {
		if (mapGrid[f.getX()][f.getY()] == f) {
	        mapGrid[f.getX()][f.getY()] = null;
	    }

	    int newX, newY;
	    do {
	        
	    	newX = RandomGenerator.getNextInt(gridSize);
	        newY = RandomGenerator.getNextInt(gridSize);
	    } 
	    while (mapGrid[newX][newY] != null);
	    f.setX(newX);
	    f.setY(newY);
	    mapGrid[newX][newY] = f;
	    System.out.println("The food was eaten and respawned its new place! " + newX + ", " + newY);
	}
	/**
	 * 
	 * @return: player(predator) who is in the game
	 */
	public Predator getPlayer() {
		return player;
	}

	
	/**
	 * This method enables the movement of the preys. adjuts the animals arraylist to be able to call the
	 * move method of Prey class with appropriate parameters then calls it after prey move, after prey 
	 * moves, this method update the grid states
	 */
	private void moveAllPrey() {
	    ArrayList<Animal> preys = new ArrayList<>();
	    for(Animal a : animals) {
	        if(a instanceof Prey) {
	        	preys.add(a);
	        }
	    }
	    
	    for(Animal p : preys) {
	        if(!animals.contains(p)) {
	        	continue;
	        }
	        
	        int pastX = p.getX();
	        int pastY = p.getY();
	        
	        ArrayList<Animal> huntersOfPreys = new ArrayList<>();
	        ArrayList<Food> foods = new ArrayList<>();
	        
	        for(Animal a : animals) {
	            if(a instanceof Predator || a instanceof ApexPredator) {
	            	huntersOfPreys.add(a);
	            }
	        }
	        for(int i = 0; i < gridSize; i++) {
	            for(int j = 0; j < gridSize; j++) {
	                if(mapGrid[i][j] instanceof Food) {
	                    foods.add((Food) mapGrid[i][j]);
	                }
	            }
	        }
	        
	        ((Prey) p).move(huntersOfPreys, foods, gridSize);
	        Logger.log(this.currentRound, "AI (PREY): " + p.getName() + 
	        		" moved to (" + p.getX() + "," + p.getY() + ")");
	        
	       
	        updateGridState(p, pastX, pastY); 
	    }
	}

	/**
	 * adjust the targetsOfApex arraylist to be able to call catchTarget method of the ApexPredator, 
	 * update the grid states after apexes moved
	 */
	private void moveAllApex() {
	    ArrayList<Animal> apexes = new ArrayList<>();
	    for(Animal a : animals) {
	        if(a instanceof ApexPredator) {
	        	apexes.add(a);
	        }
	    }
	    
	    for(Animal apex : apexes) {
	        if(!animals.contains(apex)) {
	        	continue;
	        }
	        int pastX = apex.getX();
	        int pastY = apex.getY();

	        ArrayList<Animal> targetsOfApex = new ArrayList<>();
	        for(Animal t : animals) {
	            if(t != apex && (t instanceof Prey || t instanceof Predator)) {
	                targetsOfApex.add(t);
	            }
	        }
	        
	        ((ApexPredator) apex).catchTarget(targetsOfApex, gridSize);
	        Logger.log(this.currentRound, "AI (APEX): " + apex.getName() + 
	        		" moved to (" + apex.getX() + "," + apex.getY() + ")");
	        updateGridState(apex, pastX, pastY);
	    }
	}
	

	/**
	 * this method checks the collisions and respawn the mover object or victim object deciding by hierarchy
	 * @param mover: moving animal
	 */
	private void resolveCollision(Animal mover) {
	    int x = mover.getX();
	    int y = mover.getY();
	    
	    Animal victim = null;
	    for(Animal other : animals) {
	        if(mover != other && other.getX() == x && other.getY() == y) {
	            victim = other;
	            break;
	        }
	    }

	    if (victim != null) {
	        if (mover instanceof Predator && victim instanceof Prey) {
	            this.predatorScore += 3;
	            this.preyScore -= 1;
	            Logger.log(this.currentRound, "EVENT: Hunter CAUGHT Prey at (" + x + "," + y + ") | Score: " + predatorScore);
	            respawnAnimal(victim);
	        }
	        else if (mover instanceof ApexPredator) {
	            this.apexPScore += 1;
	            if (victim instanceof Prey) {
	                this.preyScore -= 1;
	                Logger.log(this.currentRound, "EVENT: Apex CAUGHT Prey at (" + x + "," + y + ")");
	               
	                respawnAnimal(victim);
	            } 
	            else if (victim instanceof Predator) {
	                this.predatorScore -= 1;
	                Logger.log(this.currentRound, "EVENT: Apex CAUGHT PLAYER at (" + x + "," + y + ")");
	                respawnAnimal(victim);
	            }
	        }
	        else if (mover instanceof Prey) {
	             if (victim instanceof Predator) {
	                 this.predatorScore += 3;
	                 this.preyScore -= 1;
	                 respawnAnimal(mover);
	             } 
	             else if (victim instanceof ApexPredator) {
	                 this.apexPScore += 1;
	                 this.preyScore -= 1;
	                 respawnAnimal(mover);
	             }
	        }
	    }
	    
	}
	
	

	/**
	 * 
	 * @return: true if game finished, false if game is not finished
	 */
	public boolean isGameOver() {
	    return isGameFinished;
	}
	/**
	 * Reads foodchain.txt, finds the section for the current Era,
	 * and randomly selects ONE COMPLETE FOOD CHAIN line.(there are 2 in the sample txt.file)
	 * This ensures the animals are consistent with each other as defined in the file.
	 * * @return A String array containing names:ApexName, PredatorName, PreyName, FoodName
	 */
	private String[] getNamesFromFoodChain() {
	    String[] names = {"Apex", "Predator", "Prey", "Food"};
	    
	    ArrayList<String> validSetLines = new ArrayList<>();
	    File file = new File("foodchain.txt");
	    
	    String targetEraHeader = "Era: " + this.era.toString(); 

	    try (Scanner scanner = new Scanner(file)) {
	        boolean eraFound = false;

	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            
	            if (line.isEmpty()) {
	            	continue;
	            }

	            if (line.equalsIgnoreCase(targetEraHeader)) {
	                eraFound = true;
	                continue;
	            }
	            if (eraFound) {
	                if (line.toUpperCase().startsWith("ERA:")) {
	                    break; 
	                }
	                
	                if (line.startsWith("Food Chain")) {
	                    if (line.contains(":")) {
	                        String chainData = line.substring(line.indexOf(":") + 1).trim();
	                        validSetLines.add(chainData);
	                    }
	                }
	            }
	        }
	        
	        
	        if (!validSetLines.isEmpty()) {
	            
	            String selectedChain = validSetLines.get(RandomGenerator.getNextInt(validSetLines.size()));
	            
	            String[] parts = selectedChain.split(",\\s*");
	            
	            if (parts.length >= 4) {
	                names[0] = parts[0];
	                names[1] = parts[1];
	                names[2] = parts[2];
	                names[3] = parts[3];
	                System.out.println("Loaded Chain for " + this.era + ": " + selectedChain);
	            }
	        } else {
	            System.out.println("No food chains found for era: " + this.era);
	        }

	    } catch (FileNotFoundException e) {
	        System.err.println("Error: foodchain.txt not found! Using default names.");
	    }
	    
	    return names;
	}

	public String getCurrentApexName() {
	    for (Animal a : animals) {
	        if (a instanceof ApexPredator) {
	            return a.getName();
	        }
	    }
	    return "Apex Predator"; 
	}

	public String getCurrentPreyName() {
	    for (Animal a : animals) {
	        if (a instanceof Prey) {
	            return a.getName();
	        }
	    }
	    return "Prey";
	}
	/**
	 * Saves the current game information to a txt file whose name is in the form of username_save.txt,
	 * saves the scores, round infos,  era ınfos
	 * @param username: The name of the logged-in user
	 */
	public void saveGame(String username) {
	    String fileName = username + "_save.txt"; 
	    try (FileWriter writer = new FileWriter(fileName)) {
	        writer.write(this.era + "," + this.currentRound + "," + this.totalRound + "," + 
	                     this.predatorScore + "," + this.apexPScore + "," + this.preyScore + "\n");

	        for (Animal a : animals) {
	            String type = "";
	            int cooldown = 0;
	            if (a instanceof Predator) {
	                type = "PREDATOR";
	                cooldown = ((Predator) a).getCurrentCoolDown();
	            }
	            else if (a instanceof ApexPredator) {
	                type = "APEX";
	                cooldown = ((ApexPredator) a).getCurrentCoolDown();
	            }
	            else if (a instanceof Prey) {
	                type = "PREY";
	                cooldown = ((Prey) a).getCurrentCoolDown();
	            }
	            
	            writer.write(type + "," + a.getName() + "," + a.getX() + "," + a.getY()
	            + "," + cooldown + "\n");
	        }
	        for (int i = 0; i < gridSize; i++) {
	            for (int j = 0; j < gridSize; j++) {
	                if (mapGrid[i][j] instanceof Food) {
	                    Food f = (Food) mapGrid[i][j];
	                    writer.write("FOOD," + f.getName() + "," + f.getX() + "," + f.getY() + ",0\n");
	                    // since food object does not have maxCooldown variable ı wrote 0 for it
	                }
	            }
	        }
	        JOptionPane.showMessageDialog(null, "Game Saved Successfully!");

	    } 
	    catch (IOException e) {
	        JOptionPane.showMessageDialog(null, "Error while saving game: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	/**
	 * Loads the game information from a txt file whose name is in the form of username_save.txt
	 * @param username: The name of the logged-in user
	 */
	public void loadGame(String username) {
	    String filename =  username + "_save.txt"; 
	    File file = new File(filename);
        if (!file.exists()) {
	        JOptionPane.showMessageDialog(null, "There is no saved game in the memory for the user: " + username);
	        return;
	    }
	    try (Scanner scanner = new Scanner(file)) {
	        if (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            String[] parts = line.split(",");
	            
	            this.era = Era.valueOf(parts[0]); //  converting str to a Enum (Era)
	            this.currentRound = Integer.parseInt(parts[1]);
	            this.totalRound = Integer.parseInt(parts[2]);
	            this.predatorScore = Integer.parseInt(parts[3]);
	            this.apexPScore = Integer.parseInt(parts[4]);
	            this.preyScore = Integer.parseInt(parts[5]);
	        }

	        animals.clear();// clear the last game position before the player clicks the LOAD button
	        for(int i=0; i<gridSize; i++) {
	            for(int j=0; j<gridSize; j++) {
	                mapGrid[i][j] = null;
	            }
	        }

	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            String[] parts = line.split(",");
	            if (parts.length != 5) {
	                throw new LoadGameException("Invalid data format. 5 values expected but found " + 
	            parts.length + ".\nInvalid Line: "+ line);
	            }
	            
	            String type = parts[0];
	            String name = parts[1];
	            int x = Integer.parseInt(parts[2]);
	            int y = Integer.parseInt(parts[3]);
	            int cooldown = Integer.parseInt(parts[4]);

	            Animal loadedAnimal = null;

	            if (type.equals("PREDATOR")) {
	                Predator p = new Predator(name, x, y,this.era);
	                p.setCurrentCoolDown(cooldown); 
	                this.player = p;
	                loadedAnimal = p;
	            } 
	            else if (type.equals("APEX")) {
	            	ApexPredator apex = new ApexPredator(name, x, y, this.era);
	            	apex.setCurrentCoolDown(cooldown);
	                loadedAnimal = apex;
	            } 
	            else if (type.equals("PREY")) {
	            	Prey prey = new Prey(name, x, y, this.era);
	            	prey.setCurrentCoolDown(cooldown);
	                loadedAnimal = prey;
	            }
	            else if (type.equals("FOOD")) {
	                Food food = new Food(name, x, y, this.era);
	                mapGrid[x][y] = food;
	            } 
	            if (loadedAnimal != null) {
	                addAnimalToGrid(loadedAnimal);
	            }
	        }
	        
	        if (gamePanel != null) {
	            gamePanel.updateBackground(this.era);
	            gamePanel.repaint();
	        }
            JOptionPane.showMessageDialog(null, "Game Loaded Successfully! Welcome back");

	    }
	    catch (LoadGameException e) {
	        JOptionPane.showMessageDialog(null, "Save File Corrupted!" + e.getMessage());
	    }
	    catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Error loading game: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	/**
	 * Returns the current cooldown of the Apex Predator on the map.
	 * @return cooldown value or 0 if not found.
	 */
	public int getApexCooldown() {
	    for (Animal a : animals) {
	        if (a instanceof ApexPredator) {
	        	int apexCD = ((ApexPredator) a).getCurrentCoolDown();
	            return apexCD;
	        }
	    }
	    return 0;
	}

	/**
	 * Returns the current cooldown of the Prey on the map.
	 * @return cooldown value or 0 if not found.
	 */
	public int getPreyCooldown() {
	    for (Animal a : animals) {
	        if (a instanceof Prey) {
	        	int preyCD = ((Prey) a).getCurrentCoolDown();
	            return preyCD;
	        }
	    }
	    return 0;
	}
}
