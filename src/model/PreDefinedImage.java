package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Yang Yang Lu
 *
 *         PreDefinedImage extends PaintObject and draws an image (sky.png) with point
 *         from PaintObject.
 */

public class PreDefinedImage extends PaintObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PreDefinedImage(Color c, Point one, Point two) {
		super(c, one, two);
	}

	@Override
	public void draw(Graphics g) {
		try {
			if (getOne().x < getTwo().x || getOne().y < getTwo().y)
				g.drawImage(ImageIO.read(new File("sky.png")), getOne().x, getOne().y, getTwo().x - getOne().x, getTwo().y - getOne().y, null);
			else
				g.drawImage(ImageIO.read(new File("sky.png")), getOne().x, getOne().y, getOne().x - getTwo().x, getOne().y - getTwo().y, null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
