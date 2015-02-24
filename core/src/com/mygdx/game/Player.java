package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;

public class Player extends GameObject{

	protected float runspeed = 0.5f;
	protected float energy = 100;
	protected int rotation = 0;
	protected int health = 100;
	
	public Player(GameScreen screen) {
        super(screen, "ego-01.png", new Vector2(100f,100f));
	}
}
