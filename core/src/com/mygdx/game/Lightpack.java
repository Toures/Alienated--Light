package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Lightpack extends GameObject {

	public boolean consumed = false;
	
	public Lightpack(GameScreen screen, String textur, Vector2 position) {
		super(screen, textur, position);
		// TODO Auto-generated constructor stub
	}
	
	public void update(float dt) {
		if( 20 > screen.player.worldPosition.dst(worldPosition) && !consumed && screen.player.lightpacks < 5) {
			screen.player.lightpacks = Math.min(5, screen.player.lightpacks);
			consumed = true;
		}
	}

}
