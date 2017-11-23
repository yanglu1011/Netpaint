package model;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * @author Yang Yang Lu
 *
 *         Oval extends PaintObject and draws an oval with point and
 *         color from PaintObject.
 */

public class Oval extends PaintObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Oval(Color c, Point one, Point two) {
		super(c, one, two);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getC());

		if (getOne().x < getTwo().x || getOne().y < getTwo().y)
			g.fillOval(getOne().x, getOne().y, getTwo().x - getOne().x, getTwo().y - getOne().y);
		else
			g.fillOval(getTwo().x, getTwo().y, getOne().x - getTwo().x, getOne().y - getTwo().y);
	}

}
