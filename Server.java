import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;

/* Author: Haoyu(Jerry) Wu
 * A chat server that delivers public and private messages by using multi-threading.
 */
public class Server {
    
    // Create a socket for the server 
    private static ServerSocket serverSocket = null;
    // Create a socket for the user 
    private static Socket userSocket = null;
    // Maximum number of users 
    private static int maxUsersCount = 6;
    // An array of threads for users
    private static userThread[] threads = new userThread[maxUsersCount];

    public static void main(String args[]) {
        
        // The default port number.
        int portNumber = 8000;

        if (args.length < 1) {
            System.out.println("Usage: java Server <portNumber>\n"
                                   + "Now using port number=" + portNumber + "\n"
                                   + "Maximum user count = " + maxUsersCount + "\n");
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
        }
        
        // Open a server socket on the portNumber (default 8000). 
        try {
        	serverSocket = new ServerSocket(portNumber);
        	}
        	catch (IOException e) {
        		System.out.println(e);
        	}

        /*
         * Create a user socket for each connection and pass it to a new user
         * thread.
         */
        while (true) {
        	try {
        		// Create a socket to listen for and accept connection from clients
        		userSocket = serverSocket.accept();
        		
        		// pass it to a new user thread
        		int i = 0;
        		for (i = 0; i< maxUsersCount; i++) {
        			if (threads[i] == null) {
        				(threads[i] = new userThread(userSocket, threads)).start();
        				break;
        			}
        		}
        		
        		// limit the number of users
        		if (i == maxUsersCount) {
        			PrintStream output_stream = new PrintStream(userSocket.getOutputStream());
        			output_stream.println("Number of users exceeds the limit. Try again later.");
        			output_stream.close();
        			userSocket.close();
        		}        		
        	}
        	catch (IOException e) {
        		System.out.println(e);
        	}
        }
    }
}

/*
 * Threads
 */
class userThread extends Thread {
    
    private String userName = null;
    private BufferedReader input_stream = null;
    private PrintStream output_stream = null;
    private Socket userSocket = null;
    private final userThread[] threads;
    private int maxUsersCount;
    
    public userThread(Socket userSocket, userThread[] threads) {
        this.userSocket = userSocket;
        this.threads = threads;
        maxUsersCount = threads.length;
    }
    
    public void run() {

	/*
	 * Create input and output streams for this client, and start conversation.
	 */
    	
    	try {
    		// Receive input from client
            input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
            
            // Send information to the client
            output_stream = new PrintStream(userSocket.getOutputStream());
            
            // Create the user name and send the welcome message to this user
            output_stream.println("Enter the name.");
            userName = input_stream.readLine();
            while (userName.startsWith("@") == true){
            	output_stream.println("Can't start the name with @. Try another one.");
            	output_stream.println("Enter the name.");
                userName = input_stream.readLine();
                if (userName.startsWith("@") == false) {
                	break;
                }
            }
            output_stream.println("Welcome " + userName + " to our chatroom.\n"
            					+ "To leave enter LogOut in a new line.");
            
            // Broadcast the welcome message
            synchronized (this) {
            	for (int i = 0; i < maxUsersCount; i++) {
                	if (threads[i] != null && threads[i] != this) {
                		threads[i].output_stream.println("A new user " + userName + " entered the chat room!!!");
                	}
                }
            }
            
            // Broadcast or unicast the message and end the conversation when user want to logout
            while (true) {
            	String inputMessage = input_stream.readLine();
            	if (inputMessage.equals("LogOut")) {
            		break;
            	}
            	if (inputMessage.startsWith("@") == true) {
            		String actualMessage;
            		int specifiedUser = 0;
            		for (int i = 0; i < maxUsersCount; i++){
            			if(threads[i] != null && inputMessage.indexOf(threads[i].userName) != -1) {
            				specifiedUser = i;
            				}
            			}
            		actualMessage = inputMessage.split("\\s", 2)[1];
            		this.output_stream.println("<" + userName + ">" + actualMessage);
            		threads[specifiedUser].output_stream.println("<" + userName + ">" + actualMessage);
            		}
            		
            	else {
            		synchronized (this) {
            			for (int i = 0; i < maxUsersCount; i++) {
                			if (threads[i] != null) {
                				threads[i].output_stream.println("<" + userName + ">" + inputMessage);
                				}
                			}
            			}
            		}	
            	}
            
            // Broadcast the leaving message
            synchronized (this) {
            for (int i = 0; i < maxUsersCount; i++) {
            	if (threads[i] != null && threads[i] != this) {
            		threads[i].output_stream.println("*** The user " + userName + " is leaving the chat room!!!***");
            		}
            	}
            }
            
            // Send the leaving message to this user and empty the correspond thread
    		output_stream.println("### Bye " + userName + " ###");
    		for (int i = 0; i < maxUsersCount; i++) {
    			if (threads[i] == this) {
    				threads[i] = null;
    			}
    		}
    		
    		// Close the output stream, close the input stream, close the socket.
    		input_stream.close();
        	output_stream.close();
        	userSocket.close();
    	}
    	catch (IOException e) {
    		System.out.println(e);
    	}
        
    }
}





