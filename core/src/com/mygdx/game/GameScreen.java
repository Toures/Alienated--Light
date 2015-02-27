package com.mygdx.game;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameScreen implements Screen {
	private static final String SOLID = "Solid";
	public static final int TILESIZE = 32;
    private static final int DOOR_COOLDOWN = 1;
    private Meteorstorm parent;

    private SpriteBatch batch;
    private SpriteBatch fow;
	private BitmapFont font;
	private float w;
	private float h;
    boolean creditsShown=false;
    boolean creditsEnabled=false;
    boolean doorsEnabled=false;
    boolean eInterducted = false;
	
	//Objectlists
	private List<Creep> creeps = new ArrayList<Creep>();
	private List<Healthpack> healthpacks = new ArrayList<Healthpack>();
	private List<Lightpack> lightpacks = new ArrayList<Lightpack>();
	private List<Light> lights = new ArrayList<Light>();
	private List<Trigger> triggers = new ArrayList<Trigger>();
	protected Player player;
	//Pixmaps
	private Texture fowtexture;
	private Texture fowtexturebig;
	private Texture lighttexture;
	private Texture blindtexture;
    private Texture credits;
	//Misc
	private Random randGenerator = new java.util.Random(System.currentTimeMillis());
    protected MyMap tiledMap;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private boolean paused;
    boolean dPressed=false;
    private  float doorTimer = DOOR_COOLDOWN;
    private Sound music;
    private Sound batssound;
    private Sound lightdrop;
    public Sound doorsactive;
    public Sound doortriger;
    public Sound plop;
    public List<TypeWriter> typewriters=new ArrayList<TypeWriter>();
    private long batsid;

    public float getH() {
		return h;
	}

	public float getW() {
		return w;
	}

	public GameScreen(Meteorstorm parent){
        this.parent=parent;
		//music = Gdx.audio.newSound(Gdx.files.internal("music.mp3"));
		//crash = Gdx.audio.newSound(Gdx.files.internal("crash.ogg"));
		//music.loop(0.3f);
	}

	@Override
	public void render(float dt) {
		if(paused)
			return;
		update(dt);
		draw();
	}

	private void update(float dt) {

        if(creditsEnabled==true){

            if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
               creditsEnabled=false;
                return;
            }
            return;
        }


		doorTimer+=dt;

		 if(Gdx.input.isKeyPressed(Input.Keys.PLUS)){
             parent.setScreen(parent.pause);
             return;
         }
		
		/* ------------------ */
		/* ----- Player ----- */
		/* ------------------ */
		
		//Drop Lightpack
		if (Gdx.input.isKeyPressed(Input.Keys.A) && player.lightpacks > 0 && player.lightpacksCooldown == 0) {
			lights.add(new Light(this, player.worldPosition));
			player.lightpacks--;
			player.lightpacksCooldown = 2f;
            lightdrop.play();
		}
		player.lightpacksCooldown = Math.max(0, player.lightpacksCooldown-dt);
		
		//Sprinting
		int speedFactor = 1;
		float oldy= player.ySpeed;
        player.ySpeed=0;
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) &&
			(Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
			Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
			Gdx.input.isKeyPressed(Input.Keys.UP) ||
			Gdx.input.isKeyPressed(Input.Keys.DOWN)) &&
			player.energy > 5) {
			speedFactor = 3;
			player.energy -= 20*dt;
		} else
			player.energy = Math.min(player.energy + 10*dt, 100);
		
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

            doortriger.play();

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
		
		//Alien - Light interaction
		for(Light light : lights) {
			light.update(dt);
			for(Creep creep : creeps) {
				if( 100 > light.worldPosition.dst(creep.worldPosition) && light.active) {
					creep.blinded = true;
					if(light.lifetime > 5) {
						//Dass hier nichts passiert sorgt daf�r, dass die creeps eingefroren sind.
//						Vector2 diff = new Vector2(light.worldPosition).sub(creep.worldPosition).scl(-1);
//						creep.xSpeed = Math.max(-1, Math.min(1, diff.x));
//						creep.ySpeed = Math.max(-1, Math.min(1, diff.y));
					} else {
						creep.moveTo(light.worldPosition, dt);
						creep.huntingMode=false;
						creep.huntingRange=50;
					}
				} else if(creep.blinded){
					creep.blinded = false;
					creep.moveTo(creep.path.get(creep.waypoint), dt);
				}
			}
		}
		//float shortestdist=1000000000;
		//Update-calls
		for(Creep creep : creeps) {
			if(!creep.blinded)
				creep.update(dt);
         //   shortestdist=creep.getDistance()<shortestdist?creep.getDistance():shortestdist;
        }
        //float vol=(float)((Math.E)-Math.log(-shortestdist/10));
       // System.out.println(shortestdist);
       // System.out.println(vol);
        //batssound.setVolume(batsid,vol);
        for(TypeWriter t : typewriters) {
                t.update(dt);
        }
		for(Healthpack healthpack : healthpacks) {
			healthpack.update(dt);
		}
		for(Lightpack lightpack : lightpacks) {
			lightpack.update(dt);
		}
		
		for(Trigger trigger : triggers) {
			trigger.update(dt);
		}
		camera.update();
        if (!player.alive) {
            parent.setScreen(parent.gameover);
        }

	}

	private void draw() {

        if(creditsEnabled==true){
            batch = new SpriteBatch();
            batch.begin();
            batch.draw(credits,0,0);
            batch.end();
            return;
        }
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
			if(!healthpack.consumed) {
				healthpack.draw(batch);
				batch.draw(lighttexture, healthpack.worldPosition.x-100+healthpack.getWidth()/2, healthpack.worldPosition.y-100+healthpack.getHeight()/2);
			}
		}
		for(Lightpack lightpack : lightpacks) {
			if(!lightpack.consumed) {
				lightpack.draw(batch);
				batch.draw(lighttexture, lightpack.worldPosition.x-100+lightpack.getWidth()/2, lightpack.worldPosition.y-100+lightpack.getHeight()/2);
			}
		}
		
		for(Light light : lights) {
			if(light.active) {
				light.draw(batch);
				batch.draw(lighttexture, light.worldPosition.x-100+light.getWidth()/2, light.worldPosition.y-100+light.getHeight()/2);
				if(light.lifetime > 5) {
					batch.draw(lighttexture, light.worldPosition.x-100+light.getWidth()/2, light.worldPosition.y-100+light.getHeight()/2);
					batch.draw(lighttexture, light.worldPosition.x-150+light.getWidth()/2, light.worldPosition.y-150+light.getHeight()/2,300,300);
				}
				if(light.lifetime > 4) {
					batch.draw(lighttexture, light.worldPosition.x-100+light.getWidth()/2, light.worldPosition.y-100+light.getHeight()/2);
				}
			}
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
		boolean extralight = false;
		for(Light light : lights)
			if(player.worldPosition.dst(light.worldPosition) < 150 && light.active)
				extralight = true;
		if(extralight)
			fow.draw(fowtexturebig, 0, 0);
		else
			fow.draw(fowtexture, 0, 0);
        for(Light light : lights) {
			if(light.active) {
				if(light.lifetime > 5) {
					fow.draw(blindtexture, 0, 0);
				}
			}
        }

        for(TypeWriter t : typewriters) {
            t.draw(fow,font);
        }
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
		
		fow.begin();
		font.draw(fow, "Lightpacks: " + player.lightpacks + "/5", 20, 85);
		font.draw(fow, "Position:  " + (int)player.worldPosition.x/32 + "/" + (int)player.worldPosition.y/32, 700, 20);
		fow.end();
	}
	
	public void triggerEvent(int triggerID) {
		
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
        credits = new Texture(Gdx.files.internal("screens/credits.jpg"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();

        doorsactive = Gdx.audio.newSound(Gdx.files.internal("doorsactive.mp3"));
        doortriger = Gdx.audio.newSound(Gdx.files.internal("doortrigger.mp3"));
        lightdrop = Gdx.audio.newSound(Gdx.files.internal("lightdrop.wav"));
        plop = Gdx.audio.newSound(Gdx.files.internal("plop.wav"));
        tiledMap =new MyMap("MapCampaign.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap.map);
        player = new Player(this);
        player.worldPosition=new Vector2(63*32,16*32);
        
        //Creeps
        creeps.add(new Creep(this, new Vector2(55*32,16*32)));
        creeps.get(0).path.add(new Vector2(55*32,21*32));
        creeps.get(0).path.add(new Vector2(60*32,20*32));
        creeps.add(new Creep(this, new Vector2(38*32,11*32)));
        creeps.get(1).path.add(new Vector2(38*32,11*32));
        creeps.get(1).path.add(new Vector2(41*32,13*32));
        
        //Consumables
        healthpacks.add(new Healthpack(this, new Vector2(8*32,7*32)));
        healthpacks.add(new Healthpack(this, new Vector2(3*32,35*32)));
        healthpacks.add(new Healthpack(this, new Vector2(54*32,7*32)));
        lightpacks.add(new Lightpack(this, new Vector2(33*32,19*32)));
        lightpacks.add(new Lightpack(this, new Vector2(17*32,8*32)));
        lightpacks.add(new Lightpack(this, new Vector2(5*32,36*32+10)));
        lightpacks.add(new Lightpack(this, new Vector2(56*32,8*32+10)));

        //Fog of War
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

        pixmap = new Pixmap((int) w,(int) h, Format.RGBA8888 );
        pixmap.setBlending(Blending.None);
        pixmap.setColor( 0, 0, 0, 1 );
        pixmap.fill();
        pixmap.setColor(0, 0, 0, 0.6f);
        pixmap.fillCircle( (int)(w/2+player.getWidth()/2), (int)(h/2-player.getHeight()/2), 220);
        pixmap.setColor(0, 0, 0, 0.3f);
        pixmap.fillCircle( (int)(w/2+player.getWidth()/2), (int)(h/2-player.getHeight()/2), 180);
        pixmap.setColor(0, 0, 0, 0f);
        pixmap.fillCircle( (int)(w/2+player.getWidth()/2), (int)(h/2-player.getHeight()/2), 140);
        fowtexturebig = new Texture(pixmap);
        pixmap.setBlending(Blending.SourceOver);
        pixmap.dispose();

        //Lightcone
        pixmap = new Pixmap(200,200, Format.RGBA8888 );
        pixmap.setBlending(Blending.None);
        pixmap.setColor(0.5f, 1, 1, 0.05f);
        pixmap.fillCircle( 100, 100, 100);
        pixmap.setColor(0.5f, 1, 1, 0.1f);
        pixmap.fillCircle( 100, 100, 66);
        pixmap.setColor(0.5f, 1, 1, 0.15f);
        pixmap.fillCircle( 100, 100, 33);
        lighttexture = new Texture(pixmap);
        pixmap.setBlending(Blending.SourceOver);
        pixmap.dispose();

        //Blindingscreen
        pixmap = new Pixmap((int) w,(int) h, Format.RGBA8888 );
        pixmap.setColor( 0.8f, 1, 1, 0.15f );
        pixmap.fill();
        blindtexture = new Texture(pixmap);
        pixmap.dispose();
        TypeWriter doortext=new TypeWriter(this,"press [D] to open doors",new Vector2(20,h-20),20);
        doortext.time=-15;
        typewriters.add(doortext);

        music = Gdx.audio.newSound(Gdx.files.internal("music.mp3"));
        music.loop();
        //batssound = Gdx.audio.newSound(Gdx.files.internal("music.mp3"));
        // batsid = batssound.loop();
       // batssound.setVolume(batsid,0);

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