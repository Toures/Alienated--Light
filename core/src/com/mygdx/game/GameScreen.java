package com.mygdx.game;

import java.util.*;

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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
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
    private static final int DOOR_COOLDOWN = 1;
    private Meteorstorm parent;
    private SpriteBatch batch;
    private SpriteBatch fow;
    private Texture fowtexture;
	private BitmapFont font;
	protected Player player;
	private NPC npc;
	private float w;
	private float h;
	private List<Creep> creeps = new ArrayList<Creep>();
	private List<Healthpack> healthpacks = new ArrayList<Healthpack>();
	private List<Lightpack> lightpacks = new ArrayList<Lightpack>();
	private List<MeteorAnimation> anim = new ArrayList<MeteorAnimation>();
	private Random randGenerator = new java.util.Random(System.currentTimeMillis());
    protected MyMap tiledMap;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private boolean paused;
    boolean dPressed=false;
    private  float doorTimer = DOOR_COOLDOWN;
    private Sound music;
	
	public float getH() {
		return h;
	}

	public float getW() {
		return w;
	}

	public GameScreen(Meteorstorm parent){
        this.parent=parent;

	}

	@Override
	public void render(float dt) {
		if(paused)
			return;
		update(dt);
		draw();
	}

	private void update(float dt) {
		
		doorTimer+=dt;

		 if(Gdx.input.isKeyPressed(Input.Keys.R)){
             parent.setScreen(parent.pause);
             return;
         }
		
		/* ------------------ */
		/* ----- Player ----- */
		/* ------------------ */
		
		//Drop Lightpack
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.lightpacks > 0) {
			
		}
		
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
        if(player.wallCollision(dt))
        	player.xSpeed = 0;

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
		
		//CollisionDetection Player x-Axis
        if(player.wallCollision(dt))
        	player.ySpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.D)&&!dPressed&&doorTimer>DOOR_COOLDOWN) {
            Iterator it = tiledMap.doors.values().iterator();

            for (Vector2 key : tiledMap.doors.keySet())
            {
                tiledMap.setDoor((int)key.x,(int)key.y,!tiledMap.getDoor((int)key.x,(int)key.y).open);
            }
            dPressed=true;
            doorTimer=0;
        }else{
            dPressed=false;
        }

        //CollisionDetection Player y-Axis
        if(player.wallCollision(dt)) {
            player.ySpeed = 0;
        }
		
        //Update camera and everything else
		camera.position.set(player.worldPosition, camera.position.z);
		player.update(dt);
		for(Creep creep : creeps) {
			creep.update(dt);
		}
		for(Healthpack healthpack : healthpacks) {
			healthpack.update(dt);
		}
		for(Lightpack lightpack : lightpacks) {
			lightpack.update(dt);
		}
		camera.update();
        if (!player.alive) {
            parent.setScreen(parent.gameover);
        }

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
		
		//Creeps and Consumables
		for(Creep creep : creeps) {
			creep.draw(batch,creep.getRotation());
		}
		for(Healthpack healthpack : healthpacks) {
			if(!healthpack.consumed)
				healthpack.draw(batch);
		}
		for(Lightpack lightpack : lightpacks) {
			if(!lightpack.consumed)
				lightpack.draw(batch);
		}
		
		//Player
		int rotation = (int)new Vector2(player.xSpeed,player.ySpeed).angle();
			if(rotation == 0 && !Gdx.input.isKeyPressed(Input.Keys.RIGHT))
				rotation = player.rotation;
			else
				player.rotation = rotation;
		player.draw(batch, rotation);
				
		batch.end();

		layer[0] = 1;
		tiledMapRenderer.render(layer);
		
		//Fog of War
		fow.begin();
        fow.draw(fowtexture, 0, 0);
        fow.end();
		
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
		font.draw(batch, "" + creeps.get(0).isPlayerNear(100), 100, 100);
		font.draw(batch, "" + creeps.get(0).path.get(0), 100, 80);
		batch.end();
		fow.begin();
		font.draw(fow, "Lightpacks: " + player.lightpacks + "/5", 20, 85);
		font.draw(fow, ""+lightpacks.get(0).consumed, 20, 120);
		fow.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
        font = new BitmapFont();
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        fow = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();
        tiledMap =new MyMap("Map3.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap.map);
        player = new Player(this);

        creeps.add(new Creep(this, new Vector2(15*32,12*32)));
        creeps.get(0).path.add(new Vector2(25*32,13*32));
        creeps.get(0).path.add(new Vector2(13*32,13*32));

        //Consumables
        healthpacks.add(new Healthpack(this, new Vector2(4*32,7*32)));
        lightpacks.add(new Lightpack(this, new Vector2(8*32,7*32)));

        Pixmap pixmap = new Pixmap((int) w,(int) h, Format.RGBA8888 );
        pixmap.setBlending(Blending.None);
        pixmap.setColor( 0, 0, 0, 1 );
        pixmap.fill();
        pixmap.setColor(0, 0, 0, 0.6f);
        pixmap.fillCircle( (int)(w/2+player.getWidth()/2), (int)(h/2-player.getHeight()/2), 160);
        pixmap.setColor(0, 0, 0, 0.3f);
        pixmap.fillCircle( (int)(w/2+player.getWidth()/2), (int)(h/2-player.getHeight()/2), 120);
        pixmap.setColor(0, 0, 0, 0f);
        pixmap.fillCircle( (int)(w/2+player.getWidth()/2), (int)(h/2-player.getHeight()/2), 80);
        fowtexture = new Texture(pixmap);
        pixmap.setBlending(Blending.SourceOver);
        pixmap.dispose();

        music = Gdx.audio.newSound(Gdx.files.internal("music.mp3"));
        music.loop();

	}

	@Override
	public void hide() {
		music.stop();

	}

	@Override
	public void pause() {
		paused = true;

	}

	@Override
	public void resume() {
		paused = false;

	}

	@Override
	public void dispose() {
		batch.dispose();
        this.dispose();
	}
}