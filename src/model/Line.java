package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * @author Yang Yang Lu
 *
 *         Line extends PaintObject and draws a line with point and
 *         color from PaintObject.
 */

public class Line extends PaintObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Line(Color c, Point one, Point two) {
		super(c, one, two);
	}

	public void draw(Graphics g) {
		g.setColor(getC());
		g.drawLine(getOne().x, getOne().y, getTwo().x, getTwo().y);
	}
}
