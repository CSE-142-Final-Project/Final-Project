package com.csefinalproject.github.multiplayer.behaviour.client.chat;

import com.csefinalproject.github.multiplayer.behaviour.client.ClientRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles chat messages and turns it into a chat box
 */
public class ChatBox {
    private static final int DEFAULT_MAX_MESSAGES = 5;
    private static final int DEFAULT_DISTANCE_BETWEEN_MESSAGES = 15;

    private final int maxMessages;
    private final int distanceBetweenMessages;
    private final ClientRenderer clientRenderer;
    private final List<String> messages;

    /**
     * This constructor is used to create a new Chat Box (It uses the {@link ChatBox#DEFAULT_MAX_MESSAGES} and {@link ChatBox#DEFAULT_DISTANCE_BETWEEN_MESSAGES})
     * @param clientRenderer The client renderer
     */
    public ChatBox(ClientRenderer clientRenderer) {
        this(clientRenderer, DEFAULT_MAX_MESSAGES, DEFAULT_DISTANCE_BETWEEN_MESSAGES);
    }

    /**
     * This constructor is used to create a new chat box
     * @param clientRenderer The client renderer
     * @param maxMessages The max messages that can be in the chat box
     * @param distanceBetweenMessages The distance between each message
     */
    public ChatBox(ClientRenderer clientRenderer, int maxMessages, int distanceBetweenMessages) {
        this.clientRenderer = clientRenderer;
        this.maxMessages = maxMessages;
        this.distanceBetweenMessages = distanceBetweenMessages;
        this.messages = new ArrayList<>();
    }

    /**
     * Draws the chat box to the drawing panel
     */
    public void updateChatBox() {
        // Get the graphics
        Graphics g = this.clientRenderer.getDrawingPanel().getGraphics();

        // Run through all the messages in the list
        String[] messagesArray = messages.toArray(messages.toArray(new String[0]));
        for(int i = 0; i < messagesArray.length; i++) {
            // Draw each message at the bottom of the screen using the distance between messages
            g.drawString(messagesArray[i], 2, this.clientRenderer.getScreenHeight() - (this.distanceBetweenMessages * i + 2));
        }
    }

    /**
     * Add a message to the chat box
     * @param message The message to add (username and all)
     */
    public void addMessage(String message) {
        // Add to the beginning
        this.messages.add(0, message);

        // If it exceeds the length, remove the message.
        if(this.messages.size() >= this.maxMessages) {
            this.messages.remove(this.maxMessages);
        }
    }

    /**
     * Get the client renderer
     * @return The client renderer
     */
    public ClientRenderer getClientRenderer() {
        return clientRenderer;
    }
}
