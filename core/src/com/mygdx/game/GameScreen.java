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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameScreen implements Screen {
	private static final String SOLID = "Solid";
	public static final int TILESIZE = 32;
    private Game parent;
    private SpriteBatch batch;
	private BitmapFont font;
	private Player player;
	private float w;
	private float h;
	private List<Meteor> meteors = new ArrayList<Meteor>();
	private List<MeteorAnimation> anim = new ArrayList<MeteorAnimation>();
	private Random randGenerator = new java.util.Random(System.currentTimeMillis());
	private Texture img;
    private MyMap tiledMap;
    private MapLayer ground;
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
        tiledMap =new MyMap("Map2.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap.map);
        ground = tiledMap.map.getLayers().get(0);
		player = new Player(this);
		
		
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
		
		//if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		
		int speedFactor = 1;
		float oldy= player.ySpeed;
        player.ySpeed=0;
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && player.energy > 5) {
			speedFactor = 2;
			player.energy -= 20;
		} else
			player.energy = Math.min(player.energy + 8, 100);
		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.xSpeed < player.runspeed*speedFactor) {
			player.xSpeed += 0.05;
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.xSpeed > -player.runspeed*speedFactor) {
			player.xSpeed -= 0.05;
		} else if(player.xSpeed < 0.15 && player.xSpeed > -0.15) {
			player.xSpeed = 0;
		} else {
			player.xSpeed /= 1.3;
		}

        for (int i = 0; i < MyMap.MAPWIDTH; i++) {
            for (int j = 0; j < MyMap.MAPHEIGHT ; j++) {


                TiledMapTileLayer layer =(TiledMapTileLayer)tiledMap.map.getLayers().get(0);
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if(((String)cell.getTile().getProperties().get("SOLID")).equals("1")) {
                    if(Intersector.overlaps(player.calculateHitbox(player.calculateNewWorldPosition(dt)),tiledMap.getRectTile(i,j))) {
                       player.xSpeed = 0;
                    }
                }

            }
        }

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
        for (int i = 0; i < MyMap.MAPWIDTH; i++) {
            for (int j = 0; j < MyMap.MAPHEIGHT ; j++) {


                TiledMapTileLayer layer =(TiledMapTileLayer)tiledMap.map.getLayers().get(0);
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if(((String)cell.getTile().getProperties().get("SOLID")).equals("1")) {
                    if(Intersector.overlaps(player.calculateHitbox(player.calculateNewWorldPosition(dt)),tiledMap.getRectTile(i,j))) {
                        player.ySpeed = 0;
                    }
                }

            }
        }
		
		camera.translate(player.xSpeed*speedFactor*dt*player.SPEED, player.ySpeed*speedFactor*dt*player.SPEED);
			

		player.update(dt);
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

	private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        
        drawBars();
		
		ShapeRenderer energyBar = new ShapeRenderer();
		energyBar.begin(ShapeType.Filled);
		energyBar.setColor(0.0f, 0.5f, 1f, 0f);
		energyBar.rect(18, 20, 104, 22);
		energyBar.end();
		
		energyBar = new ShapeRenderer();
		energyBar.begin(ShapeType.Filled);
		energyBar.setColor(0.5f, 1f, 1f, 0f);
		energyBar.rect(20, 22, (int) player.energy, 18);
		energyBar.end();
        
		batch.begin();
		
		int rotation = (int)new Vector2(player.xSpeed,player.ySpeed).angle();
		
		//Interface
//		font.draw(batch, "Rotation: " + rotation, 20, 80);
//		font.draw(batch, "Health: " + player.health, 20, 60);
//		font.draw(batch, "Energy: " + (int) player.energy, 20, 40);
		if(rotation == 0 && !Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			rotation = player.rotation;
		else
			player.rotation = rotation;
		player.draw(batch, rotation);
		
		batch.end();
	}
	
	private void drawBars() {
		ShapeRenderer energyBar = new ShapeRenderer();
		energyBar.begin(ShapeType.Filled);
		energyBar.setColor(0.0f, 0.5f, 1f, 0f);
		energyBar.rect(18, 20, 104, 22);
		energyBar.end();
		
		energyBar = new ShapeRenderer();
		energyBar.begin(ShapeType.Filled);
		energyBar.setColor(0.5f, 1f, 1f, 0f);
		energyBar.rect(20, 22, (int) player.energy, 18);
		energyBar.end();
		
		ShapeRenderer healthBar = new ShapeRenderer();
		healthBar.begin(ShapeType.Filled);
		healthBar.setColor(0.6f, 0f, 0f, 0f);
		healthBar.rect(18, 45, 104, 22);
		healthBar.end();
		
		healthBar = new ShapeRenderer();
		healthBar.begin(ShapeType.Filled);
		healthBar.setColor(1f, 0f, 0f, 0f);
		healthBar.rect(20, 47, (int) player.health, 18);
		healthBar.end();
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

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            camera.translate(-32,0);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(32,0);
        if(keycode == Input.Keys.UP)
            camera.translate(0,-32);
        if(keycode == Input.Keys.DOWN)
            camera.translate(0,32);
        if(keycode == Input.Keys.NUM_1)
            tiledMap.map.getLayers().get(0).setVisible(!tiledMap.map.getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            tiledMap.map.getLayers().get(1).setVisible(!tiledMap.map.getLayers().get(1).isVisible());
        return false;
    }
}
