package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.io.IOException;

public class Player extends Entity {
	private final int SIZE = 64;

	public Player(String name, String pathToTexture, Point position) {
		super(name, pathToTexture, position);
	}

	@Override
	public void Draw(DrawingPanel panel, Graphics g) {
		super.Draw(panel, g);

		g.drawString(this.getName(), this.getPosition().x - 45, this.getPosition().y - 35);

		g.setColor(Color.BLACK);
		g.drawImage(getTexture(), this.getPosition().x - (SIZE / 2), this.getPosition().y - (SIZE / 2), SIZE, SIZE, null, null);
	}
}
