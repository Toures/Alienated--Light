package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by floatec on 2/26/15.
 */
public class PauseScreen implements Screen {
    Meteorstorm game;
    SpriteBatch batch;
    Texture texture;
    Texture load;
    boolean start =false;

    public PauseScreen(Meteorstorm game){
        this.game=game;
        this.texture = new Texture(Gdx.files.internal("screens/paused.jpg"));
        this.load = new Texture(Gdx.files.internal("screens/loadingscreen.jpg"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(start){
            game.setScreen(game.game);
        }else {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                start = true;
            }
            batch = new SpriteBatch();
            batch.begin();
            batch.draw(start ? load : texture, 0, 0);

            batch.end();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
