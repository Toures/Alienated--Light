package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class TypeWriter {
    public static final float SPEED = 8f;
    protected Vector2 position;
    protected GameScreen screen;
    protected String text;
    public float time=0;
    private float timeToStay=10;

    public TypeWriter(GameScreen screen, String text, Vector2 position,float timeToStay){
        this.screen = screen;
        this.text = text;
        this.position = position;
        this.timeToStay=timeToStay;

    }



    public void draw(SpriteBatch batch, BitmapFont font) {
        font.scale(2);
        int chars =Math.abs((int)(time*SPEED))<text.length()?((int)(time*SPEED)):text.length();
        if (chars<0){
            chars=0;
        }
        if(time<timeToStay) {
            font.draw(batch, text.substring(0, chars), position.x, position.y);
        }
        font.scale(-2);
    }
    


    public void update(float dt) {
    	time+=dt;
    }
    


}
