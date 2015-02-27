package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by floatec on 2/26/15.
 */
public class WinScreen implements Screen {
    Meteorstorm game;
    SpriteBatch batch;
    Texture texture;
    Texture load;
    boolean start =false;

    public WinScreen(Meteorstorm game){
        this.game=game;

    }

    @Override
    public void show() {
        this.texture = new Texture(Gdx.files.internal("screens/congrats.jpg"));
    }

    @Override
    public void render(float delta) {
        if(start){
            Gdx.app.exit();
        }else {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                start = true;
            }
            batch = new SpriteBatch();
            batch.begin();
            batch.draw( texture, 0, 0);

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
