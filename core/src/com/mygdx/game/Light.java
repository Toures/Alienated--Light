package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Light extends GameObject {

	float lifetime = 10f;
	boolean active = true;
	
	public Light(GameScreen screen, Vector2 position) {
		super(screen, "glowstick/leuchtstab-01.png", position);
		// TODO Auto-generated constructor stub
	}
	
	public void update(float dt) {
		lifetime -= dt;
		if(lifetime <= 0)
			active = false;
	}

}
