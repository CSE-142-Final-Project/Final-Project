package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;

public class ClientRenderer {
	private int screenWidth = 854;
	private int screenHeight = 480;

	private DrawingPanel drawingPanel;

	public ClientRenderer() {
		System.out.println("[CLIENT] Creating DrawingPanel.");
		this.drawingPanel = new DrawingPanel(this.screenWidth, this.screenHeight);
	}

	public ClientRenderer(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		System.out.println("[CLIENT] Creating DrawingPanel.");
		this.drawingPanel = new DrawingPanel(this.screenWidth, this.screenHeight);
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
