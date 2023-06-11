package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import java.awt.*;
import java.util.List;

public class ClientRenderer {
	private final int screenWidth;
	private final int screenHeight;

	private final DrawingPanel drawingPanel;

	public ClientRenderer() {
		this(DrawingPanel.DEFAULT_WIDTH, DrawingPanel.DEFAULT_HEIGHT);
	}

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
	}

	public void DrawEntities(List<Entity> entities) {
		Graphics g = this.drawingPanel.getGraphics();

		// Reset screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.drawingPanel.getWidth(), this.drawingPanel.getHeight());

		// Draw all the entities
		for(Entity entity : entities) {
			entity.Draw(this.drawingPanel, g);
		}

		this.drawingPanel.sleep(1);
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}
}
