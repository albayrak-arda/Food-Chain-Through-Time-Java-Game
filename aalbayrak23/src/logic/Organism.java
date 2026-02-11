package logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 * An Abstract class which has abstract and concrete subclasses, 
 * All types of food and animals are derived from this class.
 */
public abstract class Organism {
	
	protected String name;
	protected int x;
	protected int y;
	protected Era era;
	protected Image sprite;
	
	/**
	 * the constructor of the superclass(highest degree in hiearchy) of the Organism class. We cannot create
	 * a object of the Abstract Organism class. However, we can call this constructor in the subclasses' 
	 * constructor.
	 * @param name : indicates name of the organism(ex: Prey1 , Food2..)
	 * @param x : indicates the x coordinate of the organism( like (3,.))
	 * @param y : indicates the y coordinate of the organism( like (.,2))
	 * @param era : indicates the era which the organism belongs to
	 */
	public Organism(String name, int x , int y, Era era) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.era=era;
		
		try {
	        String filename = "resources/" + name.toLowerCase().trim() + ".png";
	        java.io.File file = new java.io.File(filename);

	        if (file.exists()) {
	            this.sprite = javax.imageio.ImageIO.read(file);
	        } 
	        else {
	            this.sprite = null;
	        }
	    } 
	    catch (Exception e) {
	        this.sprite = null;
	        e.printStackTrace();
	    }
	}

	/**
	 * We can change the current x coordinate of the organism by using this method.
	 * @param x : x coordinate of organism(location)
	 */
	public void setX(int x) {
		this.x = x;
	}
	
    /**
     *  We can change the current y coordinate of the organism by using this method.
     * @param y
     */
	public void setY(int y) {
		this.y = y;
	}
    /**
     * We will use this method in some of the other classes to access name of the organism and use it.
     * @return name of the organism.
     */
	public String getName() {
		return name;
	}
    /**
     * We will use this method when we need to x coordinate of the organism in the other classes.
     * @return x coordinate of the organism.
     */
	public int getX() {
		return x;
	}
    /**
     *  We will use this method when we need to y coordinate of the organism in the other classes.
     * @return y coordinate of the organism
     */
	public int getY() {
		return y;
	}
	
	public void draw(Graphics g, int x, int y, int size) {
	    if (this.sprite != null) {
	        g.drawImage(this.sprite, x, y, size, size, null);
	    } 
	    else {
	        g.setColor(Color.ORANGE); // if sprite does not exist, this line draws a orange circle
	        g.fillOval(x + 2, y + 2, size - 4, size - 4); 
	        //for the circle to be smaller than the square
	    }
	}

}
