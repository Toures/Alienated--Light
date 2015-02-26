package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Creep extends NPC {

	float attackCooldown;
	float huntingRange;
	boolean huntingMode;
	boolean blinded;
	
	public Creep(GameScreen screen, Vector2 position) {
        super(screen, "ego-01.png", position);
        huntingRange = 100;
        huntingMode = false;
	}
	
	public void update(float dt) {
		
		if(waypointReached()) {
			if(waypoint+1 >= path.size())
				waypoint = 0;
			else
				waypoint++;
			currentWaypoint.set(path.get(waypoint));
		}
		
		if(isPlayerNear(huntingRange)) {
			if(!huntingMode) {
				huntingRange = 200;
				huntingMode = true;
				speedFactor = 1;
			}
			moveTo(screen.player.worldPosition, dt);
			if(isPlayerNear(15) && attackCooldown == 0) {
				attackCooldown = 1;
				screen.player.doHealth(-20);
				huntingRange = 200;
			}
			else
				attackCooldown = Math.max(0, attackCooldown-dt);
			huntingRange = huntingRange - dt*8;
		}
		else {
			moveTo(currentWaypoint,dt);
			speedFactor = 0.6f;
			huntingRange = Math.min(huntingRange + dt*8, 100);
			huntingMode = false;
		}
		
	}
}
