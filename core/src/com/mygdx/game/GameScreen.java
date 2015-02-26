package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameScreen implements Screen {
	private static final String SOLID = "Solid";
	public static final int TILESIZE = 32;
    private Game parent;
    private SpriteBatch batch;
	private BitmapFont font;
	protected Player player;
	private NPC npc;
	private float w;
	private float h;
	private List<Creep> creeps = new ArrayList<Creep>();
	private List<MeteorAnimation> anim = new ArrayList<MeteorAnimation>();
	private Random randGenerator = new java.util.Random(System.currentTimeMillis());
    private MyMap tiledMap;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
	
	public float getH() {
		return h;
	}

	public float getW() {
		return w;
	}

	public GameScreen(Game parent){
        this.parent=parent;
		font = new BitmapFont();
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();
        tiledMap =new MyMap("Map3.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap.map);
		player = new Player(this);
		//npc = new NPC(this);
		creeps.add(new Creep(this, new Vector2(300,300)));
		creeps.add(new Creep(this, new Vector2(100,400)));
		creeps.add(new Creep(this, new Vector2(400,100)));
		creeps.get(0).path.add(new Vector2(315,310));
		creeps.get(1).path.add(new Vector2(110,415));
		creeps.get(2).path.add(new Vector2(415,110));
		
		//music = Gdx.audio.newSound(Gdx.files.internal("music.mp3"));
		//crash = Gdx.audio.newSound(Gdx.files.internal("crash.ogg"));
		//music.loop(0.3f);
	}

	@Override
	public void render(float dt) {
		update(dt);
		draw();
	}

	private void update(float dt) {
		
		//TODO if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		
		/* ------------------ */
		/* ----- Player ----- */
		/* ------------------ */
		
		//Sprinting
		int speedFactor = 1;
		float oldy= player.ySpeed;
        player.ySpeed=0;
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && player.energy > 5) {
			speedFactor = 3;
			player.energy -= 20*dt;
		} else
			player.energy = Math.min(player.energy + 8*dt, 100);
		
		//Controls x-Axis
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.xSpeed < player.runspeed*speedFactor) {
			player.xSpeed += 0.05;
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.xSpeed > -player.runspeed*speedFactor) {
			player.xSpeed -= 0.05;
		} else if(player.xSpeed < 0.15 && player.xSpeed > -0.15) {
			player.xSpeed = 0;
		} else {
			player.xSpeed /= 1.3;
		}

		//CollisionDetection Player x-Axis
        if(wallColission(dt)) {
            player.xSpeed = 0;
        }

		//Controls y-Axis
        player.ySpeed=oldy;
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.ySpeed < player.runspeed*speedFactor) {
			player.ySpeed += 0.05;
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.ySpeed > -player.runspeed*speedFactor) {
			player.ySpeed -= 0.05;
		} else if(player.ySpeed < 0.05 && player.ySpeed > -0.05) {
			player.ySpeed = 0;
		} else {
			player.ySpeed /= 1.3;
		}
		
		//CollisionDetection Player y-Axis
        if(wallColission(dt)) {
            player.ySpeed = 0;
        }
		
        //Update camera and everything else
		camera.position.set(player.worldPosition, camera.position.z);
		player.update(dt);
		//npc.update(dt);
		for(Creep creep : creeps) {
			creep.update(dt);
		}
		camera.update();
		
		
//        if(randGenerator.nextInt(100) < 40){
//			meteors.add(new Meteor(this));
//		}
//
//		for (Meteor meteor : meteors) {
//			meteor.update(dt);
//		}
//		for (int i = meteors.size()-1; i >= 0; i--) {
//			if(meteors.get(i).position.x<0){
//				meteors.remove(i);
//			}
//		}
//		for (int i = meteors.size()-1; i >= 0; i--) {
//			if(meteors.get(i).isColliding(player.position.x, player.position.y, player.getWidth(), player.getHeight())){
//				anim.add(new MeteorAnimation(meteors.get(i).position.x, meteors.get(i).position.y, meteors.get(i).scale));
//				meteors.remove(i);
//				crash.play();
//				
//			}
//		}
//		for (MeteorAnimation meteorAnimation : anim) {
//			meteorAnimation.update(dt);
//		}
//		for (int i = anim.size()-1; i >= 0; i--) {
//			if(!anim.get(i).isRunnung()){
//				anim.remove(i);
//				
//			}
//		}

	}

    private boolean wallColission(float dt){
        for (int i = Math.max(0,(int)(player.worldPosition.x/32)-1);
             i <= Math.min((int)(player.worldPosition.x/32)+1, MyMap.MAPWIDTH);
             i++) {
            for (int j = Math.max(0,(int)(player.worldPosition.y/32)-1);
                 j <= Math.min((int)(player.worldPosition.y/32)+1, MyMap.MAPWIDTH);
                 j++) {

                TiledMapTileLayer layer =(TiledMapTileLayer)tiledMap.map.getLayers().get(0);
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if(cell.getTile().getProperties().get("SOLID") != null) {
                    if(((String)cell.getTile().getProperties().get("SOLID")).equals("1")) {
                        if(Intersector.overlaps(player.calculateHitbox(player.calculateNewWorldPosition(dt)),tiledMap.getRectTile(i,j))) {
                           return true;
                        }
                    }
                }

            }
        }
        return false;
	}

	private void draw() {
		//Clear
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Tileset
        camera.update();
        tiledMapRenderer.setView(camera);
        int[] layer = {0};
        tiledMapRenderer.render(layer);
        //Objects
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		//Player
		int rotation = (int)new Vector2(player.xSpeed,player.ySpeed).angle();
		if(rotation == 0 && !Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			rotation = player.rotation;
		else
			player.rotation = rotation;
		player.draw(batch, rotation);
		//npc.draw(batch);
		for(Creep creep : creeps) {
			creep.draw(batch,creep.getRotation());
		}
		batch.end();
		
		layer[0] = 1;
		tiledMapRenderer.render(layer);
		
		drawBars();
	}
	
	private void drawBars() {
		ShapeRenderer energyBar = new ShapeRenderer();
		energyBar.begin(ShapeType.Filled);
		energyBar.setColor(0.0f, 0.5f, 1f, 0f);
		energyBar.rect(18, 20, 104, 22);
		energyBar.end();
		energyBar.dispose();
		
		energyBar = new ShapeRenderer();
		energyBar.begin(ShapeType.Filled);
		energyBar.setColor(0.5f, 1f, 1f, 0f);
		energyBar.rect(20, 22, (int) player.energy, 18);
		energyBar.end();
		energyBar.dispose();
		
		ShapeRenderer healthBar = new ShapeRenderer();
		healthBar.begin(ShapeType.Filled);
		healthBar.setColor(0.6f, 0f, 0f, 0f);
		healthBar.rect(18, 45, 104, 22);
		healthBar.end();
		healthBar.dispose();
		
		healthBar = new ShapeRenderer();
		healthBar.begin(ShapeType.Filled);
		healthBar.setColor(1f, 0f, 0f, 0f);
		healthBar.rect(20, 47, (int) player.health, 18);
		healthBar.end();
		healthBar.dispose();
		
		batch.begin();
		font.draw(batch, "" + creeps.get(0).isPlayerNear(), 100, 100);
		font.draw(batch, "" + creeps.get(0).path.get(0), 100, 80);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
        this.dispose();
	}
}