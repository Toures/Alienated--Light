package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Lightpack extends GameObject {

	public boolean consumed = false;
	
	public Lightpack(GameScreen screen, Vector2 position) {
		super(screen, "glowstick/glowstickbox-01.png", position);
		scale = 1.5f;
	}
	
	public void update(float dt) {
		if( 20 > screen.player.worldPosition.dst(worldPosition) && !consumed && screen.player.lightpacks < 5) {
			screen.player.lightpacks = Math.min(5, screen.player.lightpacks+3);
			consumed = true;
            screen.plop.play();
		}
	}

}
