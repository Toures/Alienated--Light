package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class NPC extends GameObject {

	protected List<Vector2> path = new ArrayList<Vector2>();
	protected int waypoint;
	protected Vector2 currentWaypoint = new Vector2();
	
	
	public NPC(GameScreen screen) {
        super(screen, "ego-01.png", new Vector2(200f,200f));
        path.add(new Vector2(0f,100f).add(worldPosition));
        path.add(new Vector2(100f,0f).add(worldPosition));
        path.add(new Vector2(0f,-100f).add(worldPosition));
        path.add(new Vector2(-100f,0f).add(worldPosition));
        waypoint = 0;
        currentWaypoint.set(path.get(waypoint));
	}
	
	public NPC(GameScreen screen, String textur, Vector2 position) {
        super(screen, textur, position);
        waypoint = 0;
        currentWaypoint.set(worldPosition);
	}
	
	public void update(float dt) {
		
		if(waypointReached()) {
			if(waypoint+1 >= path.size())
				waypoint = 0;
			else
				waypoint++;
			currentWaypoint.set(path.get(waypoint));
		}
		
		moveTo(currentWaypoint,dt);
		
	}
	
	public void moveTo(Vector2 destination, float dt) {
		if(destination.x > worldPosition.x)
			xSpeed += 0.02;
		else
			xSpeed -= 0.02;
		if(destination.y > worldPosition.y)
			ySpeed += 0.02;
		else
			ySpeed -= 0.02;
		
		xSpeed = Math.min(1, xSpeed);
		ySpeed = Math.min(1, ySpeed);
		
    	worldPosition = calculateNewWorldPosition(dt);
        hitbox.setPosition(worldPosition);
	}
	
	public boolean waypointReached() {
		if(currentWaypoint.x-20 < worldPosition.x 		&&
				currentWaypoint.x+20 > worldPosition.x 	&&
				currentWaypoint.y-20 < worldPosition.y	&&
				currentWaypoint.y+20 > worldPosition.y)
			return true;
		else
			return false;
	}
	
	public boolean isPlayerNear() {
		if( 100 > screen.player.worldPosition.dst(worldPosition))
			return true;
		else
			return false;
	}
}
