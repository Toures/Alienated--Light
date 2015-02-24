package com.mygdx.game;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyMap extends TiledMap{

	public static final int TILESIZE = 32;
	public static final int MAPHEIGHT = 40;
	public static final int MAPWIDTH = 50;

	public int getTilePostion(Vector2 worldPosition){
		return MAPWIDTH*(int)(worldPosition.y/TILESIZE)*(int)(worldPosition.x/TILESIZE);
	}
	public MapObject getTileByWorldPostion(Vector2 worldPosition,int layer){
		return getLayers().get(layer).getObjects().get(getTilePostion(worldPosition));
	}
	
	public Rectangle getRectTile(int tileNumber){
		//Vector2 pos = new Vector2(tileNumber/MAPWIDTH)
		return null;
	}
}
