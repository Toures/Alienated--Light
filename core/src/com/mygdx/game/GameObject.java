package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class GameObject {
    public static final int SPEED = 70;
    protected float xSpeed;
    protected float ySpeed;
    protected Vector2 worldPosition;
    protected Vector2 direction = new Vector2(0f,0f);
    protected Texture texture;
    protected GameScreen screen;
    protected float scale=1;
    protected Circle hitbox;


    public GameObject(GameScreen screen, String textur, Vector2 position){
        this.screen = screen;
        this.texture = new Texture(Gdx.files.internal(textur));
        this.worldPosition = position;
        this.hitbox = new Circle(position,getWidth()/2);
    }

    public float getWidth() {
        return this.texture.getWidth();
    }

    public float getHeight() {
        return this.texture.getHeight();
    }

    public void draw(SpriteBatch batch, int rotation) {

        batch.draw(texture, worldPosition.x, worldPosition.y, getWidth()/2, getHeight()/2, texture.getWidth(), texture.getHeight(), scale, scale, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }
    
    public void draw(SpriteBatch batch) {

        batch.draw(texture,worldPosition.x, worldPosition.y,texture.getWidth()*scale,texture.getHeight()*scale);
    }
    
    public void draw(SpriteBatch batch, int rotation, boolean flipX, boolean flipY) {

        batch.draw(texture, worldPosition.x, worldPosition.y, getWidth()/2, getHeight()/2, texture.getWidth(), texture.getHeight(), scale, scale, rotation, 0, 0, texture.getWidth(), texture.getHeight(), flipX, flipY);
    }
    
    public void update(float dt) {

    	//xSpeed = xSpeed - (float)(Math.sqrt((xSpeed + ySpeed)));
    	worldPosition = calculateNewWorldPosition(dt);
        hitbox.setPosition(worldPosition);
    }
    
    public Circle calculateHitbox(Vector2 newWorldPosition){
         return new Circle(newWorldPosition, getHeight()/2);
    }
    
    public Vector2 calculateNewWorldPosition(float dt){
    	
    	 Vector2 move = new Vector2(xSpeed*SPEED*dt,ySpeed*SPEED*dt);
         return worldPosition.add(move);
    }

}
