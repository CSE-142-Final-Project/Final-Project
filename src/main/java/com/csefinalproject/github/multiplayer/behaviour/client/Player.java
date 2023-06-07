package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.io.IOException;

public class Player extends Entity {
	private final int SIZE = 15;

	public Player(String name, String pathToTexture, Point position) {
		super(name, pathToTexture, position);
	}

	@Override
	public void Draw(DrawingPanel panel, Graphics g) {
		super.Draw(panel, g);

		g.setColor(Color.BLACK);
		g.drawImage(getTexture(), this.getPosition().x - (SIZE / 2), this.getPosition().y - (SIZE / 2), SIZE, SIZE, null, null);
	}
}
