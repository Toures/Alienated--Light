package com.mygdx.game;

import com.badlogic.gdx.Game;

public class Meteorstorm extends Game {
	
	@Override
	public void create() {		
		setScreen(new GameScreen(this));
	}

	@Override
	public void dispose() {
	
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
