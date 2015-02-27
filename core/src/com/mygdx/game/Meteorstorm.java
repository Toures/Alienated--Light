package com.mygdx.game;

import com.badlogic.gdx.Game;

public class Meteorstorm extends Game {
    public GameScreen game;
    public StartScreen start;
    public GameOverScreen gameover;
    public PauseScreen pause;

	@Override
	public void create() {
        start =new StartScreen(this);
        gameover= new GameOverScreen(this);
        pause =new PauseScreen(this);
		setScreen(start);

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

    public void switchToGame(){
        game=new GameScreen(this);
        setScreen(game);
    }
}
