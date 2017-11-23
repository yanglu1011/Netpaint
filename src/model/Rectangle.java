package model;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * @author Yang Yang Lu
 *
 *         Rectangle extends PaintObject and draws a rectangle with point and
 *         color from PaintObject.
 */

public class Rectangle extends PaintObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Rectangle(Color c, Point one, Point two) {
		super(c, one, two);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getC());

		if (getOne().x < getTwo().x || getOne().y < getTwo().y)
			g.fillRect(getOne().x, getOne().y, getTwo().x - getOne().x, getTwo().y - getOne().y);
		else
			g.fillRect(getTwo().x, getTwo().y, getOne().x - getTwo().x, getOne().y - getTwo().y);
	}

}
