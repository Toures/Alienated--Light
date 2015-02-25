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
        currentWaypoint = path.get(waypoint);
	}
	
	public void update(int dt) {
		
		if(waypointReached()) {
			if(waypoint >= path.size())
				waypoint = 0;
			else
				waypoint++;
		}
		
		this.direction = path.get(waypoint).sub(worldPosition);
		xSpeed = direction.x;
		ySpeed = direction.y;
    	worldPosition = calculateNewWorldPosition(dt);
        hitbox.setPosition(worldPosition);
	}
	
	public boolean waypointReached() {
		if(currentWaypoint.x-1 < worldPosition.x 		&&
				currentWaypoint.x+1 > worldPosition.x 	&&
				currentWaypoint.y-1 < worldPosition.y	&&
				currentWaypoint.y+1 > worldPosition.y)
			return true;
		else
			return false;
	}
}
