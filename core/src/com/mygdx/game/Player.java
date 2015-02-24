package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;

public class Player extends GameObject{

	protected float runspeed = 0.5f;
	protected int energy = 100;
	
	public Player(GameScreen screen) {
        super(screen, "Spaceship.png", new Vector2(100f,100f));
	}
}
