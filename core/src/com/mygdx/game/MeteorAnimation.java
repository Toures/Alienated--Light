package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MeteorAnimation {

    public static final int FRAME_COUNT = 8;
    public static final float FRAME_DURATION = 0.025f;
    Animation animation;
	Texture sheet; 
	TextureRegion[] frames;
	TextureRegion currentFrame;
	private float stateTime;
	private float x, y;
	private float scale;
	private float fulltime = 0 ;
	
	public MeteorAnimation(float x,float y,float scale) {
		this.x=x;
		this.y=y;
		this.scale=scale;
		
		sheet = new Texture(Gdx.files.internal("animation.png"));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/ FRAME_COUNT, sheet.getHeight());
        frames = new TextureRegion[FRAME_COUNT];
        int index = 0;
        for (int i = 0; i < FRAME_COUNT; i++) {
                frames[index++] = tmp[0][i];
        }
        animation = new Animation(FRAME_DURATION, frames);
                     
        stateTime = 0f;            
	}
	public void update(float dt) {
        
        stateTime += dt;
        fulltime += dt;
        currentFrame = animation.getKeyFrame(stateTime, false);
    }
	
	public void draw(SpriteBatch batch){
		batch.draw(currentFrame, x, y,sheet.getWidth()/FRAME_COUNT*scale,sheet.getHeight()*scale);
	}
	public boolean isRunnung(){
		return fulltime<=FRAME_DURATION*FRAME_COUNT;
	}
}
