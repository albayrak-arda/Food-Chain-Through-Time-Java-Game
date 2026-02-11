package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.ApexPredator;
import logic.Era;
import logic.GameEngine;
import logic.Organism;
import logic.Predator;
import logic.Prey;

public class GamePanel extends JPanel {
	private GameEngine gameEngine;
	private Image backgroundImage;
	
	
	
	/**
	 * the  constructor method of the GamePanel class, it allows the player to move with the mouse.
	 * @param gameEngine
	 */
	public GamePanel(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				if (SwingUtilities.isLeftMouseButton(e)) {
			        
					int gridSize = gameEngine.getGridSize();
		            int cellSize = Math.min(getWidth() / gridSize, getHeight() / gridSize);
		            int xOffset = (getWidth() - (gridSize * cellSize)) / 2;
		            int gridX = (e.getX() - xOffset) / cellSize;
		            int gridY = e.getY() / cellSize;
		            
		            if (gridX >= 0 && gridX < gridSize && gridY >= 0 && gridY < gridSize) {
		                
		                try {
		                    boolean moved = gameEngine.movePlayer(gridX, gridY);
		                    
		                    if (moved) {
		                        repaint(); 
		                        GameFrame frame = (GameFrame) javax.swing.SwingUtilities.getWindowAncestor(GamePanel.this);
		                        if (frame != null) {
		                            frame.updateInfos();
		                        }
		                    }
		                }
		                catch (Exception ex) {
		                    ex.printStackTrace();
		                }
		            }
		        }
			}
			
		});
	}
	
	@Override
	/**
	 * this methods draw the background of game, draw and paint grid lines, paint the squares that player
	 * can move in its next action, paint the squares which have an organism on them
	 * @param g: use it to paint
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g; 
		// I am using it to set up the thickness of the grid lines and give a transparent look to them
		int gridSize = gameEngine.getGridSize();
		int dynamicSquareDim = Math.min(getWidth() / gridSize, getHeight() / gridSize);
		int xOffset = getXOffset();
		Era currentEra = gameEngine.getEra();
		if (backgroundImage != null) {
	        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	    }
		else {// if image could not found;
			if (currentEra == Era.PAST) {
		        g.setColor(new Color(34, 139, 34));
		    } 
		    else if (currentEra == Era.PRESENT) {
		        g.setColor(new Color(144, 238, 144));
		    } 
		    else if (currentEra == Era.FUTURE) {
		        g.setColor(new Color(44, 62, 80));
		    } 
		    else {
		        g.setColor(Color.WHITE); 
		    }
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		
		
		Predator p = gameEngine.getPlayer();
	    if (p != null) {
	        boolean isApexAdjacent = gameEngine.checkApexAdjacent(); 
	        for (int i = 0; i < gridSize; i++) {
	            for (int j = 0; j < gridSize; j++) {
	                int differenceX = Math.abs(i - p.getX());
	                int differenceY = Math.abs(j - p.getY());
	                int distance = Math.max(differenceX, differenceY);

	                if (distance == 1) {// indicates possible moving squares of the predator with colors
	                    g.setColor(new Color(255, 255, 255, 80)); 
	                    g.fillRect(xOffset + i * dynamicSquareDim, j * dynamicSquareDim, dynamicSquareDim, dynamicSquareDim);
	                } 
	                else if (p.canSpecialMoveTo(i, j, gridSize, isApexAdjacent)) {// special move grids indicated by colors
	                    g.setColor(new Color(255, 255, 0, 100)); 
	                    g.fillRect(xOffset + i * dynamicSquareDim, j * dynamicSquareDim, dynamicSquareDim, dynamicSquareDim);
	                }
	            }
	        }

	        // Grid lines' colors suitable for the background
	        if (currentEra == Era.FUTURE) {
	        	g2d.setColor(new Color(0, 240, 255, 210));
	        } 
	        else if(currentEra == Era.PRESENT) {
	        	g2d.setColor(new Color(45, 25, 10, 160));
	        }
	        else {
	        	g2d.setColor(new Color(220, 220, 200, 170));
	        }
	        
	    }
		
		g2d.setStroke(new BasicStroke(3)); // grid line thickness
		for(int i=0; i<=gridSize ; i++) {
			g.drawLine(xOffset + i * dynamicSquareDim, 0, xOffset + i * dynamicSquareDim, gridSize *dynamicSquareDim);//horizontal
	        g.drawLine(xOffset, i * dynamicSquareDim, xOffset + gridSize * dynamicSquareDim, i * dynamicSquareDim);
		}
		g2d.setStroke(new BasicStroke(1));// after draw the lines reset thickness to 1 again
		
		for(int i=0; i<gridSize;i++) { // add color to grid that organism is on of it
			for(int j =0; j<gridSize;j++) {
				Organism o = gameEngine.getOrganismAtGrid(i, j);
				if( o!= null) {
					if (o instanceof Predator) {
						g2d.setColor(new Color(255, 165, 0, 50));
					}
		            else if (o instanceof Prey) {
		            	g2d.setColor(new Color(0, 255, 0, 50));
		            }
		            else if (o instanceof ApexPredator) {
		            	g2d.setColor(new Color(255, 0, 0, 70));
		            }
					g2d.fillRect(xOffset + i * dynamicSquareDim + 1, j * dynamicSquareDim + 1, dynamicSquareDim - 1, dynamicSquareDim - 1);
					o.draw(g, xOffset + i * dynamicSquareDim, j * dynamicSquareDim, dynamicSquareDim);
				}
			}
		}
	}
	
	/**
	 * this method calculates the cellSize
	 * @return: cellSize in terms of pixels
	 */
	private int calculateCellSize() {
	    int gridSize = gameEngine.getGridSize();
	    int cellSize = Math.min(getWidth() / gridSize, getHeight() / gridSize);
	    return cellSize;
	}

	/**
	 * this method calculates the horizontal total space in the game board and divide it to 2
	 *  ( left space = right space to locate board in the middle of the screen)
	 * @return: left space margin
	 */
	private int getXOffset() {
		int XOffset = (getWidth() - (gameEngine.getGridSize() * calculateCellSize())) / 2;
	    return XOffset;
	}
	

	/**
	 * this method should be called when the era changes or game starts to set up background image
	 * @param currentEra: currentEra
	 */
	public void updateBackground(Era currentEra) {
	    String fileName = "resources/bg_" + currentEra.toString().toLowerCase() + ".png";
	    // images are in the resources folder both sprites and backgrounds
	    try {
	        File imgFile = new File(fileName);
	        if (imgFile.exists()) {
	            this.backgroundImage = ImageIO.read(imgFile);
	            
	        } 
	        
	    } 
	    catch (IOException e) {
	        this.backgroundImage = null;
	    }
	    repaint();
	}

	
}