package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Healthpack extends GameObject {

	public boolean consumed = false;
	
	public Healthpack(GameScreen screen, String textur, Vector2 position) {
		super(screen, textur, position);
		// TODO Auto-generated constructor stub
	}
	
	public void update(float dt) {
		if( 20 > screen.player.worldPosition.dst(worldPosition) && !consumed) {
			screen.player.doHealth(100);
			consumed = true;
		}
	}
}
