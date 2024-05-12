package groupChatttingApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	private String username;
	
	public Client(Socket socket, String username) {
		try {
			this.socket = socket;
			this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
		} catch (IOException e) {
			closeEverything(socket, br, bw);
		}
	}
	
	public void sendMessage() {
		try {
			bw.write(username);
			bw.newLine();
			bw.flush();
			
			Scanner scanner = new Scanner(System.in);
			while (socket.isConnected()) {
				String messageToSend = scanner.nextLine();
				bw.write(username + ": " + messageToSend);
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			closeEverything(socket, br, bw);
		}
	}
	
	//listening messages from other clients from the server
	public void listenForMessage() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String msgFromGroupChat;
				
				while (socket.isConnected()) {
					try {
						msgFromGroupChat = br.readLine();
						System.out.println(msgFromGroupChat);
					} catch (IOException e) {
						closeEverything(socket, br, bw);
					}
				}	
			}
		}).start();
	}
	
	public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw) {
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
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your username ffor the group chat: ");
		String username = scanner.nextLine();
		//create socket object that will be passed to the client
		Socket socket = new Socket("localhost", 8080);
		//create client object that takes socket object
		Client client = new Client(socket, username);
		client.listenForMessage();
		client.sendMessage();
	}
}
