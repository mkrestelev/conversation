package com.conversation.service;

import com.conversation.model.Message;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The {@code SocketConnectionManager} class extends {@link ConnectionManager}
 * and is responsible for managing the communication between talkers
 * through a socket connection.
 */
public final class SocketConnectionManager extends ConnectionManager implements Closeable {

    /**
     * Default port number for the socket connection.
     */
    private static final int PORT = 9999;

    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    /**
     * Sends a message through the socket connection. Initializes client connection if needed,
     * then writes the message to the socket and reads the response.
     *
     * @param message message object to be sent
     * @return the value of the message received in response
     * @throws IOException If there are any IO exceptions during send or receive of message
     */
    @Override
    public String sendMessage(Message message) throws IOException {
        if (socket == null) {
            initializeClientConnection();
        }
        writeMessageToSocket(message);
        return readMessageFromSocket().value();
    }

    /**
     * Receives a message from the socket. Initializes server connection if needed, then reads message from the socket,
     * delegates message handling to the Talker, and sends response back.
     *
     * @return message object that is the reply to the received message
     * @throws IOException if there are any IO exceptions during receiving or sending the message
     */
    @Override
    public Message receiveMessage() throws IOException {
        if (socket == null) {
            initializeServerConnection();
        }
        Message message = readMessageFromSocket();
        Talker talker = getTalker(message.receiverId());
        Message reply = talker.receiveMessage(message);
        writeMessageToSocket(reply);
        return reply;
    }

    private void writeMessageToSocket(Message message) throws IOException {
        System.out.printf("Writing message to socket: %s%n", message.value());
        outputStream.writeObject(message);
        outputStream.flush();
    }

    private Message readMessageFromSocket() throws IOException {
        try {
            Message message = (Message) inputStream.readObject();
            System.out.printf("Received message from socket: %s%n", message.value());
            return message;
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn't read message from socket");
            throw new RuntimeException(e.getMessage());
        }
    }

    private void initializeClientConnection() throws IOException {
        this.socket = new Socket("localhost", PORT);
        this.outputStream = getOutputStream();
        this.inputStream = getInputStream();
    }

    private void initializeServerConnection() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.socket = serverSocket.accept();
        this.outputStream = getOutputStream();
        this.inputStream = getInputStream();
    }

    private ObjectOutputStream getOutputStream() throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }

    private ObjectInputStream getInputStream() throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

     /**
      * Closes socket connection.
      *
      * @throws IOException If an IO exception occurs during closure of the resources
      */
    @Override
    public void close() throws IOException {
        tryClose(inputStream);
        tryClose(outputStream);
        tryClose(serverSocket);
        tryClose(socket);
    }

    private void tryClose(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }
}
