package org.human.gulim.runcatch.server.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.human.gulim.runcatch.contants.Constants;

public class Server {
	
	private ServerSocket serverSocket;
	
	public void run() throws IOException{
		Socket socket;
		ClientRunnable runnable;
		
		serverSocket = new ServerSocket(Constants.SERVER_PORT);
		while(true)
		{
			System.out.println("[listen]: Server is waiting for connection...");
			socket = serverSocket.accept();
			System.out.println("[listen]: Connection is accepted.");
			
			runnable = new ClientRunnable();
			runnable.setSocket(socket);
			new Thread(runnable).start();
		}
	}

}
