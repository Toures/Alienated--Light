package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Healthpack extends GameObject {

	public boolean consumed = false;
	
	public Healthpack(GameScreen screen, Vector2 position) {
		super(screen, "medikit/medikit-01.png", position);
		scale = 1.5f;
	}
	
	public void update(float dt) {
		if( 20 > screen.player.worldPosition.dst(worldPosition) && !consumed && screen.player.health != 100) {
			screen.player.doHealth(100);
			consumed = true;
		}
	}
}
