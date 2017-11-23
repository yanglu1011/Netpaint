package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

import model.PaintObject;

/**
 * @author Yang Yang Lu
 * 
 *         SendCanvas sends the canvas (PaintObjects) to all clients that exist.
 */

public class SendCanvas implements Runnable {

	// all necessary variables
	private ObjectInputStream reader;
	private Socket clientSocket;
	private Vector<ObjectOutputStream> clientOutputStreams;
	private Vector<PaintObject> allPaintObjects;

	// ----------------------------------------------
	// SendCanvas - Constructor
	//
	// used to initialize the variables and reader
	// which is the inputs from clients.
	// ----------------------------------------------
	public SendCanvas(Socket clientSocket, Vector<ObjectOutputStream> clientOutputStreams,
			Vector<PaintObject> allPaintObjects) {
		this.clientOutputStreams = clientOutputStreams;

		this.allPaintObjects = allPaintObjects;

		if (this.allPaintObjects != null)
			System.out.println("paint objects: " + this.allPaintObjects.size());

		try {
			this.clientSocket = clientSocket;

			if (this.clientSocket == null) {
				JOptionPane.showMessageDialog(null, "socket is null");
			}

			reader = new ObjectInputStream(this.clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------
	// run - Public Method
	//
	// used to read input from clients and update
	// the shared canvas.
	// ----------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		// loop until the exception is caught and
		// just read and update the paint objects
		try {
			while (true) {
				if (this.allPaintObjects != null) {
					System.out.println("paint objects: " + this.allPaintObjects.size());
					updateCanvas(allPaintObjects);
				} else
					System.out.println("paint is null");

				System.out.println("Client: " + this.clientOutputStreams.size());

				allPaintObjects = (Vector<PaintObject>) reader.readObject();

				updateCanvas(allPaintObjects);
			}

		} catch (ClassNotFoundException | IOException e) {
			System.out.println("connection reset");
			e.printStackTrace();
		}
	}

	// ----------------------------------------------
	// run - Private Method
	//
	// used write the paint objects to all clients
	// that exist so that they can update their
	// Paint Objects.
	// ----------------------------------------------
	private void updateCanvas(Vector<PaintObject> allPaintObjects) {

		for (ObjectOutputStream output : clientOutputStreams) {
			try {
				output.reset();
				output.writeObject(allPaintObjects);
				output.flush();
			} catch (Exception e) {
				System.out.println("output stream removed");
				e.printStackTrace();
				clientOutputStreams.remove(output);
			}

		}

	}

	// getter for allPaintObjects
	public Vector<PaintObject> getAllPaintObjects() {
		return allPaintObjects;
	}

}
