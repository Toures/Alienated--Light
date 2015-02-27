package com.mygdx.game;

/**
 * Created by floatec on 2/26/15.
 */
public class Door {
    public boolean open;
    public int direction;
    public  boolean locked;
    public Door(boolean open,int direction,boolean locked){
        this.open=open;
        this.direction=direction;
        this.locked=locked;
    }
}
