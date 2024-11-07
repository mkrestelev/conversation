package com.game.service;

import com.game.model.Message;
import java.io.IOException;

/**
 * Talker that can send and receive messages.
 */
public final class Talker {

    private final String id;
    private final ConnectionManager connectionManager;

    /**
     *  The count of replies sent by this talker.
     *  Incremented each time a reply is sent in response to an incoming message.
     */
    private int replyCount = 0;

    public Talker(String id, ConnectionManager connectionManager) {
        this.id = id;
        this.connectionManager = connectionManager;
    }

    /**
     * Gets the unique identifier of the talker.
     *
     * @return the talker's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sends a message to another talker through the connection manager.
     *
     * @param message the message to be sent
     * @throws IOException if an error occurs during the message sending process
     */
    public void sendMessage(Message message) throws IOException {
        System.out.printf("Talker %s sends message to Talker %s: %s%n", getId(), message.receiverId(), message.value());
        String reply = connectionManager.sendMessage(message);
        System.out.printf("Talker %s received reply from Talker %s: %s%n", getId(), message.receiverId(), reply);
    }

    /**
     * Receives a message from another talker and returns a reply message.
     *
     * @param message the incoming message
     * @return a reply message including the initiator's ID and an initial message with concatenated counter
     */
    public Message receiveMessage(Message message) {
        System.out.printf("Talker %s received message from Talker %s: %s%n", getId(), message.senderId(), message.value());
        return new Message(getId(), message.senderId(), String.format("%s %s", message.value(), ++replyCount));
    }

}
