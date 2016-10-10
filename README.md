# ChatApp
A chat room consisting of one server and multiple clients

Files name: EchoUser.java EchoServer.java
The overall design & how it works: In this part, I implement a client and a server that communicate over the network using TCP.
The user can send the message (text string) to the server using the connection.
The server repeatedly accept an input message from a client and send back the same message.
When the client receives the message back from the server, it prints it and exits.

Possible tradeoffs & extension: EchoUser can be improved by allowing input multiple line messages.

Compilation instructions: Compile both files in csa2.bu.edu and run on the same port number.


Files name: Server.java User.java
The overall design & how it works: Use multi-threading to implement a chat room with one server and multiple clients.
The clients and server will communicate over the network using TCP.  The server should be running on a certain port. After a client connects to the server and sends a message, this message should be broadcasted to all clients by the server.The server has to also announce a new client joining or leaving to all other clients. The client connects to the server using the server’s port number and enters its username. Once the connection is established, the client should be able to send and receive messages as well as be able to quit the chat room. 
Then add unicast capability to the chat room. Check the username when a user enter the chatroom. If the name isn't start with "@", clients should be able to send private messages by starting with "@" in the message. Private messages are sent to a specified user and are not broadcasted.

Possible tradeoffs & extension: User can be improved by allowing to input multiple line messages. Server should check duplicate username to avoid wrong connection when a user send a private message to another user who has the same name with another user.

Compilation instructions: Compile both files in csa2.bu.edu and run on the same port number.

