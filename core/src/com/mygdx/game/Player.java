package com.mygdx.game;


import com.badlogic.gdx.math.Vector2;

public class Player extends GameObject{

	public Player(GameScreen screen) {
        super(screen, "Spaceship.png", new Vector2(100f,100f));
	}
}
