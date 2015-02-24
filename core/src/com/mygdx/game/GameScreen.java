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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class GameScreen implements Screen {
	private static final String SOLID = "Solid";
	public static final int TILESIZE = 32;
	public static final int MAPHEIGHT = 15;
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
    private TiledMap tiledMap;
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
        tiledMap = new TmxMapLoader().load("Map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        ground = tiledMap.getLayers().get(0);
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
			
		float oldySpeed = player.ySpeed;
		player.ySpeed 0 0;
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.xSpeed < 0.5) {
			player.xSpeed += 0.05;
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.xSpeed > -0.5) {
			player.xSpeed -= 0.05;
		} else if(player.xSpeed < 0.15 && player.xSpeed > -0.15) {
			player.xSpeed = 0;
		} else {
			player.xSpeed /= 1.3;
		}
		Circle newHitbox = player.calculateHitbox(player.calculateNewWorldPosition(dt));
		int tileNumber = 0;
		for(MapObject obj : ground.getObjects()){
			if ((Integer)obj.getProperties().get(SOLID)==1){
				if()
			}
			tileNumber++;
		}
		
		player.ySpeed=oldySpeed;
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.ySpeed < 0.5) {
			player.ySpeed += 0.05;
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.ySpeed > -0.5) {
			player.ySpeed -= 0.05;
		} else if(player.ySpeed < 0.05 && player.ySpeed > -0.05) {
			player.ySpeed = 0;
		} else {
			player.ySpeed /= 1.3;
		}

		player.update(dt);
		
		
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
        
		batch.begin();
		player.draw(batch, 90);
		for (Meteor meteor : meteors) {
			meteor.draw(batch);
		}
		for (MeteorAnimation meteorAnimation : anim) {
			meteorAnimation.draw(batch);
		}
		
		font.draw(batch, "Meteorstorm", 10, 20);
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
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if(keycode == Input.Keys.NUM_2)
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        return false;
    }
}
