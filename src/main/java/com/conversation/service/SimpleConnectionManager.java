package com.conversation.service;

import com.conversation.model.Message;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The {@code SimpleConnectionManager} class extends {@link ConnectionManager}
 * and is responsible for managing the communication between talkers
 * through a simple message queue.
 */
public final class SimpleConnectionManager extends ConnectionManager {

    private final Queue<Message> messages = new LinkedList<>();

    /**
     * Sends a message by adding it to the internal message queue.
     * Immediately receives a response by invoking the {@link #receiveMessage()} method.
     *
     * @param message the {@link Message} to be sent
     * @return a {@link String} representing the reply received from the respondent
     */
    @Override
    public String sendMessage(Message message) {
        messages.add(message);
        return receiveMessage().value();
    }

    /**
     * Receives the next message from the internal queue and
     * delivers it to the corresponding talker.
     *
     * @return the {@link Message} that contains the reply from the corresponding talker
     */
    @Override
    public Message receiveMessage() {
        Message message = messages.remove();
        Talker talker = getTalker(message.receiverId());
        return talker.receiveMessage(message);
    }

    /**
     * Closes the connection manager by clearing all messages from the queue.
     */
    @Override
    public void close() {
        messages.clear();
    }
}
