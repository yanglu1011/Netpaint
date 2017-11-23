package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import model.PaintObject;

/**
 * @author Yang Yang Lu
 * 
 *         Server is used to accept clients and create the shared canvas using
 *         new threads.
 */

public class Server {
	
	public static void main(String[] args) {
		new Server();
	}

	// all necessary variables
	private Vector<ObjectOutputStream> clientOutputStreams;
	private Vector<PaintObject> allPaintObjects = null;
	private SendCanvas temp = null;

	// --------------------------------------------------
	// Server - Constructor
	// 
	// used to accept clients and then create a new
	// thread to write new paint objects to all clients.
	// --------------------------------------------------
	public Server() {
		clientOutputStreams = new Vector<ObjectOutputStream>();
		
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSock = new ServerSocket(4000);

			while (true) {
				Socket clientSocket = serverSock.accept();

				ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);

				// when a new client connected if there is an old
				// client (thread) exist take in what is already
				// in that client.
				if (temp != null) {
					allPaintObjects = temp.getAllPaintObjects();
				}

				System.out.println("Client: " + this.clientOutputStreams.size());
				
				// use the existing paint objects to update the new client
				temp = new SendCanvas(clientSocket, clientOutputStreams, allPaintObjects);

				// update the actual canvas of paint objects
				allPaintObjects = temp.getAllPaintObjects();

				// start the new threads, calls run()
				Thread t = new Thread(temp);
				t.start();
				System.out.println("got a connection");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
