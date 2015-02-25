package com.mygdx.game;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyMap {
    public TiledMap map;
	public static final int TILESIZE = 32;
	public static final int MAPHEIGHT = 40;
	public static final int MAPWIDTH = 50;

    public MyMap(String file){
        map =new TmxMapLoader().load(file);
    }

	public int getTilePostion(Vector2 worldPosition){
		return MAPWIDTH*(int)(worldPosition.y/TILESIZE)*(int)(worldPosition.x/TILESIZE);
	}
	public MapObject getTileByWorldPostion(Vector2 worldPosition,int layer){
		return map.getLayers().get(layer).getObjects().get(getTilePostion(worldPosition));
	}
	
	public Rectangle getRectTile(int x,int y){
		Vector2 pos = new Vector2(x*TILESIZE,y*TILESIZE);
		return new Rectangle(pos.x,pos.y,TILESIZE,TILESIZE);
	}
}
