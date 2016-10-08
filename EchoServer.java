import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;



/* Author: Haoyu (Jerry) Wu
 * An echo server that simply echoes messages that received from the client .
 */

public class EchoServer {
    
    // Create a socket for the server 
    private static ServerSocket serverSocket = null;
    // Create a socket for the user 
    private static Socket userSocket = null;
    private static BufferedReader input_stream = null;
    private static PrintStream output_stream = null;



    public static void main(String args[]) {
        
        // The default port number.
        int portNumber = 8000;
        if (args.length < 1) {
            System.out.println("Usage: java Server <portNumber>\n"
                                   + "Now using port number=" + portNumber + "\n");
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
        }
        
        
        /*
         * Open a server socket on the portNumber (default 8000). 
         */
        try {
        	serverSocket = new ServerSocket(portNumber);
        	}
        	catch (IOException e) {
        		System.out.println(e);
        	}
        
        /*
         * Create a user socket for accepted connection and echo back the message
         */
        while (true) {
            try {
            	// Create a socket to listen for and accept connection from clients
                userSocket = serverSocket.accept();
                
                // Receive input from client
                input_stream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
                
                // Send information to the client
                output_stream = new PrintStream(userSocket.getOutputStream());
                
                // Echo back the message
                String inputLine;
                while((inputLine = input_stream.readLine()) != null) {
                	output_stream.println(inputLine);
                }
            	/*
             	* Close the output stream, close the input stream, close the socket.
             	*/
            	input_stream.close();
            	output_stream.close();
            	userSocket.close(); 
                }
            catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}





