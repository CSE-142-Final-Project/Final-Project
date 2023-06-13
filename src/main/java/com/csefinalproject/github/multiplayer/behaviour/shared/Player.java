package com.csefinalproject.github.multiplayer.behaviour.shared;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.io.IOException;

public class Player extends Entity {
	private static final String DEFAULT_PLAYER_TEXTURE = "/assets/player.png";

	public Player(String name, Point position, short clientId) {
		super(name, DEFAULT_PLAYER_TEXTURE, position, clientId);
	}

	@Override
	public void Draw(DrawingPanel panel, Graphics g) {
		super.Draw(panel, g);

		// Draw username
		g.setColor(Color.BLACK);
		g.drawString(this.getName(), this.getPosition().x - 45, this.getPosition().y - 35);
	}
}
