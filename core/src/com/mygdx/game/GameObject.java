package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        boolean isSolid;
        boolean isDoor;
        boolean isOpen=false;
        for (int i = Math.max(0,(int)(worldPosition.x/32)-1);
             i <= Math.min((int)(worldPosition.x/32)+1, MyMap.MAPWIDTH);
             i++) {
            for (int j = Math.max(0,(int)(worldPosition.y/32)-1);
                 j <= Math.min((int)(worldPosition.y/32)+1, MyMap.MAPWIDTH);
                 j++) {

                TiledMapTileLayer layer =(TiledMapTileLayer)screen.tiledMap.map.getLayers().get(0);
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                TiledMapTileLayer layer2 =(TiledMapTileLayer)screen.tiledMap.map.getLayers().get(0);
                TiledMapTileLayer.Cell cell2 = layer2.getCell(i, j);
                isSolid=false;
                if(cell!=null&&cell.getTile().getProperties().containsKey("SOLID")&& cell.getTile().getProperties().get("SOLID") != null) {
                    isSolid = ((String) cell.getTile().getProperties().get("SOLID")).equals("1");
                }
                if(cell2!=null&&cell2.getTile().getProperties().containsKey("SOLID")&&cell2.getTile().getProperties().get("SOLID") != null&&!isSolid) {
                    isSolid = ((String) cell2.getTile().getProperties().get("SOLID")).equals("1");
                }
                if(cell!=null&&cell.getTile().getProperties().containsKey("ACTION")&&cell.getTile().getProperties().get("ACTION") != null) {
                    if(!screen.eInterducted){
                        screen.eInterducted=true;
                        screen.typewriters.remove(screen.typewriters.size()-1);
                        TypeWriter doortext=new TypeWriter(screen,"ohh...a console...lets press [E] to hack it",new Vector2(20,screen.getH()-20),10);
                        screen.typewriters.add(doortext);
                    }
                    if(((String) cell.getTile().getProperties().get("ACTION")).equals("1")&&!this.screen.doorsEnabled&&Gdx.input.isKeyPressed(Input.Keys.E)){
                        if(screen.typewriters.size()>0) {
                            screen.typewriters.remove(screen.typewriters.size() - 1);
                        }

                        TypeWriter doortext2=new TypeWriter(screen,"lets hack it ......................",new Vector2(20,screen.getH()-20),2);
                        doortext2.time=0;
                        //screen.typewriters.add(doortext2);
                        TypeWriter doortext3=new TypeWriter(screen,".............yes!\nall doors are working now!",new Vector2(20,screen.getH()-20),7);
                        doortext3.time=5;
                        screen.doorsactive.play();
                        screen.typewriters.add(doortext3);
                        for (Vector2 key : screen.tiledMap.doors.keySet())
                        {
                            screen.tiledMap.getDoor((int)key.x,(int)key.y).locked=false;
                        }
                        screen.doorsEnabled=true;
                    }
                    if(((String) cell.getTile().getProperties().get("ACTION")).equals("0")&&!this.screen.creditsShown&&Gdx.input.isKeyPressed(Input.Keys.E)){
                        screen.creditsShown=true;
                        screen.creditsEnabled=true;
                    }

                }
                if(screen.tiledMap.isDoor(i,j)) {
                    isDoor = true;
                    isOpen = screen.tiledMap.getDoor(i,j).open;
                }else {
                    isDoor=false;
                }
                if(isSolid || (isDoor && !isOpen)) {
                    if (Intersector.overlaps(calculateHitbox(calculateNewWorldPosition(dt)), screen.tiledMap.getRectTile(i, j))) {
                        return true;
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
