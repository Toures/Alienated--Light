package com.mygdx.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class Creep extends NPC {

	float attackCooldown;
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
		
		if(isPlayerNear(100)) {
			moveTo(screen.player.worldPosition, dt);
			speedFactor = 1;
			if(isPlayerNear(15) && attackCooldown == 0) {
				attackCooldown = 1;
				screen.player.doHealth(-20);
			}
			else
				attackCooldown = Math.max(0, attackCooldown-dt);
				
		}
		else {
			moveTo(currentWaypoint,dt);
			speedFactor = 0.6f;
		}
		
	}
}
