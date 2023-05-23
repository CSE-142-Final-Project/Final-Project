package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;

public class ClientRenderer {
	private final int PANEL_WIDTH = 854;
	private final int PANEL_HEIGHT = 480;

	private DrawingPanel drawingPanel;

	public ClientRenderer() {
		// Create DrawingPanel
		System.out.println("[CLIENT] Creating DrawingPanel.");
		drawingPanel = new DrawingPanel(PANEL_WIDTH, PANEL_HEIGHT);
	}

	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}
}
