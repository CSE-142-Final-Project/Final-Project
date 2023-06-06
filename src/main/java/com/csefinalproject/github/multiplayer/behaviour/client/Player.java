package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import java.awt.*;

public class Player extends Entity {
	public Player(String name, String pathToTexture, Point position) {
		super(name, pathToTexture, position);
	}

	@Override
	public void Draw(DrawingPanel panel, Graphics g) {
		super.Draw(panel, g);

		g.setColor(Color.BLACK);
		g.drawRect(this.getPosition().x, this.getPosition().y, 15, 15);
	}
}
