package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

/**
 * @author Yang Yang Lu
 * 
 *         PaintObject is an abstract class that is used to
 *         draw objects such as line, oval, rectangle, etc.
 *         Have an abstract method call draw.
 */

public abstract class PaintObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color c;
	private Point one;
	private Point two;

	public PaintObject(Color c, Point one, Point two) {
		this.c = c;
		this.one = one;
		this.two = two;
	}

	public abstract void draw(Graphics g);

	//______________________________
	// getters below
	
	public Color getC() {
		return c;
	}

	public Point getOne() {
		return one;
	}

	public Point getTwo() {
		return two;
	}

}
