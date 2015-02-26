package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;

public class Player extends GameObject{

	public static final int MAXLIGHTS = 5;
	protected float runspeed = 0.6f;
	protected float energy = 100;
	protected int rotation = 0;
	protected int health = 100;
	public boolean alive = true;
	protected int lightpacks = 3;
	
	public Player(GameScreen screen) {
        super(screen, "ego-01.png", new Vector2(64f,64f));
	}
	
	public void doHealth(int health) {
		this.health = Math.min(100, Math.max(0, this.health+health));
		if(this.health == 0)
			alive = false;
	}
}
