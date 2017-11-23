package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import model.Line;
import model.Oval;
import model.PaintObject;
import model.PreDefinedImage;
import model.Rectangle;

/**
 * @author Yang Yang Lu
 * 
 *         Client is the GUI that takes input from the user using mouse inputs
 *         and client itself is connected to the server to draw on a shared
 *         canvas that is host by server.
 */

public class Client extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new Client();
	}

	// all necessary variables
	private DrawingPanel drawingPanel;
	private static Vector<PaintObject> allPaintObjects;

	// buttons
	private JButton colorChooserButton;
	private JRadioButton lineButton;
	private JRadioButton rectButton;
	private JRadioButton ovalButton;
	private JRadioButton imgButton;

	// points
	private Point one;
	private Point two;

	// output stream to server
	private ObjectOutputStream outputToServer;

	// checks on mouse clicks
	private boolean clicked = false;

	// color chooser frame
	private JColorChooser jcc;
	JFrame colorFrame = null;

	// -----------------------------------------
	// Client - Constructor
	//
	// setups on the main frame: size, location
	// close operation, and the panels.
	// -----------------------------------------
	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLocation(20, 20);
		setSize(800, 600);

		setupPanel();

		setTitle("Client");

		setVisible(true);

		// ask for connection to server
		connectToServer();
	}

	// -----------------------------------------
	// connectToServer - Private Method
	//
	// used to connect to server, which is
	// read the PaintObjects from server and
	// update the PaintObjects in client.
	// -----------------------------------------
	@SuppressWarnings("unchecked")
	private void connectToServer() {
		Socket server = null;

		try {
			server = new Socket("localhost", 4000);

			// outputToServer is used to write PaintObjects over to server
			// after the second mouse click.
			outputToServer = new ObjectOutputStream(server.getOutputStream());

			ObjectInputStream inputFromServer = new ObjectInputStream(server.getInputStream());

			// loop until client end itself
			while (true) {
				allPaintObjects = (Vector<PaintObject>) inputFromServer.readObject();
				repaint();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------
	// setupPanel - Private Method
	//
	// used to setup the entire GUI and add
	// listeners to necessary components.
	// -----------------------------------------
	private void setupPanel() {
		// main panel to be listened by mouse
		JPanel mainPanel = new JPanel(new BorderLayout());
		// button panel
		JPanel buttonPanel = new JPanel(new GridLayout(1, 6));

		// initialize all buttons
		colorChooserButton = new JButton("Choose Color");
		lineButton = new JRadioButton("Line");
		rectButton = new JRadioButton("Rectangle");
		ovalButton = new JRadioButton("Oval");
		imgButton = new JRadioButton("Image");

		// create button group to ensure single selection
		ButtonGroup group = new ButtonGroup();

		// add the 4 radio buttons to group
		group.add(lineButton);
		group.add(rectButton);
		group.add(ovalButton);
		group.add(imgButton);

		// add to buttonPanel
		buttonPanel.add(colorChooserButton);
		buttonPanel.add(lineButton);
		buttonPanel.add(rectButton);
		buttonPanel.add(ovalButton);
		buttonPanel.add(imgButton);

		// add to main frame
		// this.add(buttonPanel, BorderLayout.SOUTH);
		drawingPanel = new DrawingPanel();
		drawingPanel.setSize(this.getWidth(), this.getHeight());

		// setup the scroll pane
		JScrollPane jsp = new JScrollPane(drawingPanel);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// add JScrollPane of drawing panel and button panel
		// to the main panel
		mainPanel.add(jsp, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// add main panel to the main frame
		this.add(mainPanel, BorderLayout.CENTER);

		allPaintObjects = new Vector<PaintObject>();

		jcc = new JColorChooser();

		// default the jcc color to black
		jcc.setColor(Color.BLACK);

		// add listeners
		drawingPanel.addMouseListener(new MouseClick());
		drawingPanel.addMouseMotionListener(new MouseMove());
		colorChooserButton.addActionListener(new ButtonListener());
	}

	/**
	 * @author Yang Yang Lu
	 * 
	 *         ButtonListener - Private Class
	 * 
	 *         used to listen for button click on color chooser button.
	 *         Which then creates the color frame that stores the color
	 *         chooser.
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// if color frame doesn't exist create a new one
			// if it exist though just pull it up to the
			// front.
			if (colorFrame == null) {
				colorFrame = new JFrame();
				colorFrame.add(jcc);
				colorFrame.setSize(500, 400);
				colorFrame.setVisible(true);
				colorFrame.setLocation(getLocation());
			} else {
				colorFrame.setLocation(getLocation());
				colorFrame.setVisible(true);
			}

		}

	}
	
	/**
	 * @author Yang Yang Lu
	 * 
	 *         MouseClick - Private Class
	 * 
	 *         used to listen for mouse click on paint object buttons
	 *         which performs first click and second click. At second
	 *         click the object is write to server.
	 */
	private class MouseClick implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent mouse) {
			// all cases adds a temporary object to the vector
			// then at second click completes the actual shape.
			if (lineButton.isSelected()) {
				clicked = !clicked;
				System.out.println(mouse.getX() + ", " + mouse.getY());
				one = new Point(mouse.getX(), mouse.getY());
				two = new Point(mouse.getX(), mouse.getY());
				allPaintObjects.add(new Line(jcc.getColor(), one, two));
				repaint();
			} else if (rectButton.isSelected()) {
				clicked = !clicked;
				System.out.println(mouse.getX() + ", " + mouse.getY());
				one = new Point(mouse.getX(), mouse.getY());
				two = new Point(mouse.getX(), mouse.getY());
				allPaintObjects.add(new Rectangle(jcc.getColor(), one, two));
				repaint();
			} else if (ovalButton.isSelected()) {
				clicked = !clicked;
				System.out.println(mouse.getX() + ", " + mouse.getY());
				one = new Point(mouse.getX(), mouse.getY());
				two = new Point(mouse.getX(), mouse.getY());
				allPaintObjects.add(new Oval(jcc.getColor(), one, two));
				repaint();
			} else if (imgButton.isSelected()) {
				clicked = !clicked;
				System.out.println(mouse.getX() + ", " + mouse.getY());
				one = new Point(mouse.getX(), mouse.getY());
				two = new Point(mouse.getX(), mouse.getY());
				allPaintObjects.add(new PreDefinedImage(jcc.getColor(), one, two));
				repaint();
			} else {
				return;
			}

			// at second click
			if (!clicked) {
				// since at second click there will be another
				// temporary shape added so remove it.
				if (!allPaintObjects.isEmpty())
					allPaintObjects.remove(allPaintObjects.size() - 1);
				
				// write the object to server
				try {
					outputToServer.writeObject(allPaintObjects);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// ____________________________________________
		// not used methods below
		
		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {

		}

		@Override
		public void mousePressed(MouseEvent arg0) {

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {

		}

	}

	private class MouseMove implements MouseMotionListener {

		@Override
		public void mouseMoved(MouseEvent mouse) {
			// all cases will check on if a click have already be performed
			// and creates the ghost shapes until clicked is false by the
			// second click.
			if (lineButton.isSelected() && clicked) {
				System.out.println(mouse.getX() + ", " + mouse.getY());
				two = new Point(mouse.getX(), mouse.getY());
				allPaintObjects.remove(allPaintObjects.size() - 1);
				allPaintObjects.add(new Line(jcc.getColor(), one, two));
				repaint();
			} else if (rectButton.isSelected() && clicked) {
				System.out.println(mouse.getX() + ", " + mouse.getY());
				two = new Point(mouse.getX(), mouse.getY());
				allPaintObjects.remove(allPaintObjects.size() - 1);
				allPaintObjects.add(new Rectangle(jcc.getColor(), one, two));
				repaint();
			} else if (ovalButton.isSelected() && clicked) {
				System.out.println(mouse.getX() + ", " + mouse.getY());
				two = new Point(mouse.getX(), mouse.getY());
				allPaintObjects.remove(allPaintObjects.size() - 1);
				allPaintObjects.add(new Oval(jcc.getColor(), one, two));
				repaint();
			} else if (imgButton.isSelected() && clicked) {
				System.out.println(mouse.getX() + ", " + mouse.getY());
				two = new Point(mouse.getX(), mouse.getY());
				allPaintObjects.remove(allPaintObjects.size() - 1);
				allPaintObjects.add(new PreDefinedImage(jcc.getColor(), one, two));
				repaint();
			}
		}
		
		// not used method
		@Override
		public void mouseDragged(MouseEvent arg0) {

		}
	}

	/**
	 * This is where all the drawing goes.
	 * 
	 * @author mercer
	 */
	public class DrawingPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DrawingPanel() {
			this.setPreferredSize(new Dimension(1600, 2000));
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			// draw all of the paint objects
			for (PaintObject ob : allPaintObjects)
				ob.draw(g);
		}
	}
}
