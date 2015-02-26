package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Creep extends NPC {

	public Creep(GameScreen screen, Vector2 position) {
        super(screen, "ego-01.png", position);
	}
	
	public void update(float dt) {
		
		if(waypointReached()) {
			if(waypoint+1 >= path.size())
				waypoint = 0;
			else
				waypoint++;
			currentWaypoint.set(path.get(waypoint));
		}
		
		if(isPlayerNear())
			moveTo(screen.player.worldPosition, dt);
		else
			moveTo(currentWaypoint,dt);
		
	}
}
