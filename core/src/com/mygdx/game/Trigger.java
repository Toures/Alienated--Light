package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Trigger {

	private GameScreen screen;
	private Vector2 offset = new Vector2();
	private Vector2 dimensions = new Vector2();
	protected boolean triggered = false;
	private int triggerID = 0;
	static int idCounter = 0;
	
	public Trigger(GameScreen screen, int x, int y, int width, int height) {
		this.screen = screen;
		offset.x = x;
		offset.y = y;
		dimensions.x = width;
		dimensions.y = height;
		triggerID = idCounter;
		idCounter++;
	}
	
	public Trigger(GameScreen screen, Vector2 offset, Vector2 dimensions) {
		this.screen = screen;
		this.offset.set(offset);
		this.dimensions.set(dimensions);
		triggerID = idCounter;
		idCounter++;
	}
	
	public Trigger(GameScreen screen, Vector2 offset, int width, int height) {
		this.screen = screen;
		this.offset.set(offset);
		dimensions.x = width;
		dimensions.y = height;
		triggerID = idCounter;
		idCounter++;
	}
	
	public void update(float dt) {
		if(offset.x <= screen.player.worldPosition.x &&
				screen.player.worldPosition.x <= dimensions.x &&
				offset.y <= screen.player.worldPosition.y &&
				screen.player.worldPosition.y <= dimensions.y)
			screen.triggerEvent(triggerID);
	}

}
