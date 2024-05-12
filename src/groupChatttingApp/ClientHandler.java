package groupChatttingApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
	
	//keep track of all off the clients
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	
	//used to read and message data to clients, these will be messages that have sent from other clients
	//that will be then broadcasted with arraylist
	private BufferedReader br;
	private BufferedWriter bw;
	private String clientUsername;
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			//wrapping byte stream inside a char stream
			this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername = br.readLine();
			clientHandlers.add(this);
			broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
		} catch(IOException e) {
			closeEverything(socket, br, bw);
		}
	}
	
	@Override
	public void run() {
		String messageFromClient;
		
		while (socket.isConnected()) {
			try {
				messageFromClient = br.readLine();
				broadcastMessage(messageFromClient);
			} catch (IOException e) {
				closeEverything(socket, br, bw);
				break;
			}
		}
	}
	
	public void broadcastMessage(String messageToSend) {
		for (ClientHandler clientHandler : clientHandlers) {
			try {
				if (!clientHandler.clientUsername.equals(clientUsername)) {
					clientHandler.bw.write(messageToSend);
					clientHandler.bw.newLine();
					clientHandler.bw.flush();
				}
			} catch (IOException e) {
				closeEverything(socket, br, bw);
			}
		}
	}
	
	public void removeClientHandler() {
		clientHandlers.remove(this);
		broadcastMessage("SERVER: " + clientUsername + " has left the chat");
	}
	
	public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw) {
		removeClientHandler();
		//to handle the null ptr exception
		try {
			if (br != null) {
				br.close();
			}
			if (bw != null) {
				bw.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
