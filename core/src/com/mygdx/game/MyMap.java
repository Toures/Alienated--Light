package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class MyMap {
    public TiledMap map;
	public static final int TILESIZE = 32;
	public static final int MAPHEIGHT = 30;
	public static final int MAPWIDTH = 40;

    TiledMapTileLayer groudLayer;
    public Map<Vector2,Boolean> doors = new HashMap<Vector2,Boolean>();

    public MyMap(String file){
        map =new TmxMapLoader().load(file);
        groudLayer =(TiledMapTileLayer)map.getLayers().get(0);

        for (int i = 0; i < MAPWIDTH; i++) {
            for (int j = 0; j < MAPHEIGHT; j++) {
                TiledMapTileLayer.Cell cell = groudLayer.getCell(i, j);
                if(cell.getTile().getProperties().get("DOOR")!=null) {
                    doors.put(new Vector2(i,j),((String) cell.getTile().getProperties().get("OPEN")).equals("1"));
                }

            }
        }
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

    public void setDoor(int x, int y,boolean value){
        TiledMapTileLayer.Cell cell = groudLayer.getCell(x, y);
        cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(Gdx.files.internal("tiles/Floor/door-01"+(value?"":"-closed")+".png")))));
        doors.put(new Vector2(x,y),value);
    }
    public boolean getDoor(int x, int y){
        return doors.get(new Vector2(x,y));
    }
    public boolean isDoor(int x, int y){
        return doors.containsKey(new Vector2(x,y));
    }
}
