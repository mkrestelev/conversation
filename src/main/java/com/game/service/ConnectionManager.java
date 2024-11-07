package com.game.service;

import com.game.model.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The abstract class for managing connection between talkers.
 */
public sealed abstract class ConnectionManager implements AutoCloseable
        permits SimpleConnectionManager, SocketConnectionManager  {

    /**
     * Defines maximum number of interactions between talkers.
     */
    private static final int MAX_REPLY_COUNT = 10;

    /**
     * A map holding the talkers connected via connection manager.
     * <p>
     *     The key is the talker's ID and the value is the {@link Talker} object
     * </p>
     */
    private final Map<String, Talker> talkers = new HashMap<>();

    /**
     * Sends a message to the registered talker.
     *
     * @param message the message to be sent
     * @return the reply from message receiver
     * @throws IOException if there is an error while sending the message
     */
    public abstract String sendMessage(Message message) throws IOException;

    /**
     * Receives next available message.
     *
     * @return the received message
     * @throws IOException if there is an error while receiving the message
     */
    public abstract Message receiveMessage() throws IOException;

    /**
     * Closes all resources initiated by connection manager.
     *
     * @throws IOException if there is an error while closing resources
     */
    public abstract void close() throws IOException;

    /**
     * Registers a new talker in the connection manager.
     *
     * @param talker the talker to be registered
     */
    public void registerTalker(Talker talker) {
        this.talkers.put(talker.getId(), talker);
    }

    /**
     * Gets the maximum number of replies for a conversation managed by the connection manager.
     *
     * @return the maximum reply count
     */
    public static int getMaxReplyCount() {
        return MAX_REPLY_COUNT;
    }

    /**
     * Retrieves a talker by their ID from the connection manager.
     *
     * @param talkerId the ID of the talker to retrieve
     * @return {@link Talker} object
     */
    public Talker getTalker(String talkerId) {
        return talkers.get(talkerId);
    }
}
