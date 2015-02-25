package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class Alien extends GameObject {

	protected List<Vector2> path = new ArrayList<Vector2>();
	protected int waypoint;
	protected Vector2 currentWaypoint = new Vector2();
	
	
	public Alien(GameScreen screen) {
        super(screen, "ego-01.png", new Vector2(200f,200f));
        path.add(new Vector2(0f,100f));
        path.add(new Vector2(100f,0f));
        path.add(new Vector2(0f,-100f));
        path.add(new Vector2(-100f,0f));
        waypoint = 0;
        currentWaypoint = path.get(waypoint);
	}
	
	public void update(int dt) {
		
		direction.x = currentWaypoint.x;
    	worldPosition = calculateNewWorldPosition(dt);
        hitbox.setPosition(worldPosition);
	}
}
