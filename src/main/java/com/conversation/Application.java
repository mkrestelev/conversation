package com.conversation;

import com.conversation.model.Message;
import com.conversation.service.ConnectionManager;
import com.conversation.service.Talker;
import com.conversation.service.SimpleConnectionManager;
import com.conversation.service.SocketConnectionManager;
import java.io.IOException;

/**
 * Main application class that handles single-process and inter-process communication between {@link Talker} objects.
 */
public class Application {

    private static final String INTER_PROCESS_SERVER = "S";
    private static final String INTER_PROCESS_CLIENT = "C";

    /**
     * The main method decides the mode of operation based on the arguments provided, and then triggers the
     * appropriate communication method.
     *
     * @param args command line arguments specifying the mode of operation
     *             (No argument for single-process, 'S'/'C' for inter-process)
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            performSingleProcessCommunication();
        } else if (INTER_PROCESS_CLIENT.equals(args[0])) {
            performInterProcessCommunicationAsClient();
        } else if (INTER_PROCESS_SERVER.equals(args[0])) {
            performInterProcessCommunicationAsServer();
        }
    }

    /**
     * Handles the single-process mode where two {@link Talker} objects communicate
     * within the same application (with same PID).
     */
    private static void performSingleProcessCommunication() {
        try (ConnectionManager connectionManager = new SimpleConnectionManager()) {
            Talker initiator = new Talker("A", connectionManager);
            Talker respondent = new Talker("B", connectionManager);
            connectionManager.registerTalker(respondent);

            System.out.println("Starting conversation between Talkers");

            Message message = new Message(initiator.getId(), respondent.getId(), "Message");
            for (int i = 0; i < ConnectionManager.getMaxReplyCount(); i++) {
                initiator.sendMessage(message);
            }
            logConversationFinish();
        } catch (IOException e) {
            System.err.println("Exception occurred during conversation between Talkers");
        }
    }

    /**
     * Sets up and handles the server-side logic for inter-process communication between
     * {@link Talker} objects using socket.
     */
    private static void performInterProcessCommunicationAsServer() {
        System.out.printf("Server is started (PID %s)%n", ProcessHandle.current().pid());
        try (ConnectionManager connectionManager = new SocketConnectionManager()) {
            Talker respondent = new Talker("B", connectionManager);
            connectionManager.registerTalker(respondent);
            System.out.println("Talker B (respondent) ready for conversation");
            for (int i = 0; i < ConnectionManager.getMaxReplyCount(); i++) {
                connectionManager.receiveMessage();
            }
            logConversationFinish();
        } catch (IOException e) {
            System.err.println("Exception occurred when receiving message from socket");
        }

    }

    /**
     * Sets up and handles the client-side logic for inter-process communication between
     * {@link Talker} objects using socket.
     */
    private static void performInterProcessCommunicationAsClient() {
        System.out.printf("Client is started (PID %s)%n", ProcessHandle.current().pid());
        try (ConnectionManager connectionManager = new SocketConnectionManager()) {
            Talker initiator = new Talker("A", connectionManager);
            Message message = new Message(initiator.getId(), "B", "Message");
            System.out.println("Talker A (initiator) starts conversation");
            for (int i = 0; i < ConnectionManager.getMaxReplyCount(); i++) {
                initiator.sendMessage(message);
            }
            logConversationFinish();
        } catch (IOException e) {
            System.err.println("Exception occurred when sending message to socket");
        }
    }

    private static void logConversationFinish() {
        System.out.println("Conversation between Talkers is finished");
    }

}