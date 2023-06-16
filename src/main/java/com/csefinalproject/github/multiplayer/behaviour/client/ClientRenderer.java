package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.client.chat.ChatBox;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import java.awt.*;
import java.util.List;

/**
 * This class is used to render the game and contains all the rendering logic
 */
public class ClientRenderer {
	private final int screenWidth;
	private final int screenHeight;

	private final DrawingPanel drawingPanel;
	private final ChatBox chatBox;

	/**
	 * This constructor is used to create a new client renderer (It uses the {@link DrawingPanel#DEFAULT_WIDTH} and {@link DrawingPanel#DEFAULT_HEIGHT}
	 */
	public ClientRenderer() {
		this(DrawingPanel.DEFAULT_WIDTH, DrawingPanel.DEFAULT_HEIGHT);
	}

	/**
	 * This constructor is used to create a new client renderer
	 * @param screenWidth the width of the screen
	 * @param screenHeight the height of the screen
	 */
	public ClientRenderer(int screenWidth, int screenHeight) {
		// Catch any invalid window sizes and just set it to the default
		if(screenWidth > 0 && screenHeight > 0) {
			this.screenWidth = screenWidth;
			this.screenHeight = screenHeight;
		} else {
			throw new IllegalArgumentException("Cannot create a DrawingPanel window with a width/height less than 1.");
		}

		System.out.println("[CLIENT] Creating DrawingPanel.");
		this.drawingPanel = new DrawingPanel(this.screenWidth, this.screenHeight);

		System.out.println("[CLIENT] Creating Chat Box");
		this.chatBox = new ChatBox(this);
	}

  /**
	 * This method is used to draw all of the visuals
	 * @param entities the entities to draw
	 */
	public void UpdateVisuals(List<Entity> entities) {
		Graphics g = this.drawingPanel.getGraphics();

		// Reset screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.drawingPanel.getWidth(), this.drawingPanel.getHeight());

		// Draw all the entities
		for(Entity entity : entities) {
			entity.Draw(this.drawingPanel, g);
		}

		// Update the chat box
		this.chatBox.updateChatBox();

		this.drawingPanel.sleep(1);
	}

	/**
	 * This method is used to get the width of the screen
	 * @return the width of the screen
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * This method is used to get the height of the screen
	 * @return the height of the screen
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * This method is used to get the drawing panel
	 * @return the drawing panel
	 */
	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}

	/**
	 * @return the chat box object on the screen
	 */
	public ChatBox getChatBox() {
		return chatBox;
	}
}
