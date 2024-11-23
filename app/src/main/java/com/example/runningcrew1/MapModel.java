package com.example.runningcrew1;

import android.util.Log;

public class MapModel {

    private int displayWidth =0;
    private int displayHeight =0;

    private int terrainX;
    private int terrainY;


    public MapModel(int displayWidth, int randNum){

        this.displayWidth = displayWidth;

        this.terrainX = displayWidth-200;
        this.terrainY = randNum;
    }

    public void updatePosition(){
        this.terrainX -= 1;
        Log.d("UpdatePosition", "Map updated: X = " + this.terrainX);

    }

    public int getTerrainX(){
        return this.terrainX;
    }
    public int getTerrainY(){
        return this.terrainY;
    }
}

