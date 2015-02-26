package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class GameObject {
    public static final int SPEED = 140;
    protected float xSpeed;
    protected float ySpeed;
    protected Vector2 worldPosition;
    protected Vector2 direction = new Vector2();
    protected Texture texture;
    protected GameScreen screen;
    protected float scale=1;
    protected Circle hitbox;
	protected float speedFactor;


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
    
    public int getRotation() {
    	return (int) new Vector2(xSpeed,ySpeed).angle();
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
    	worldPosition = calculateNewWorldPosition(dt);
        hitbox.setPosition(worldPosition);
    }
    
	public boolean wallCollision(float dt){
        for (int i = Math.max(0,(int)(worldPosition.x/32)-1);
             i <= Math.min((int)(worldPosition.x/32)+1, MyMap.MAPWIDTH);
             i++) {
            for (int j = Math.max(0,(int)(worldPosition.y/32)-1);
                 j <= Math.min((int)(worldPosition.y/32)+1, MyMap.MAPWIDTH);
                 j++) {

                TiledMapTileLayer layer =(TiledMapTileLayer)screen.tiledMap.map.getLayers().get(0);
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if(cell.getTile().getProperties().get("SOLID") != null) {
                    if(((String)cell.getTile().getProperties().get("SOLID")).equals("1")) {
                        if(Intersector.overlaps(calculateHitbox(calculateNewWorldPosition(dt)),screen.tiledMap.getRectTile(i,j))) {
                           return true;
                        }
                    }
                }

            }
        }
        return false;
	}
    
    public Circle calculateHitbox(Vector2 newWorldPosition){
        return new Circle(newWorldPosition.add(getWidth()/2, getHeight()/2), getHeight()/2-4);
    }
    
    public Vector2 calculateNewWorldPosition(float dt){
    	
    	Vector2 move = new Vector2();
    	if(!direction.isZero())
    		move.set(direction).scl(SPEED*dt);
    	else
    		move.set(xSpeed*SPEED*dt,ySpeed*SPEED*dt);
    	Vector2 newPosition = new Vector2(worldPosition);
        return newPosition.add(move);
    }

}
