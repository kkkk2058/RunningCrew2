package com.example.runningcrew1;

public class MapModel {

    private float displayWidth =0;
    private float displayHeight =0;

    private float terrainX = 0;
    private float terrainY = 0;


    public MapModel(int displayWidth, int randNum){

        this.displayWidth = displayWidth;

        this.terrainX = displayWidth;
        this.terrainY = randNum;
    }

    public void updatePosition(){
        this.terrainX -= 1;
    }

    public float getTerrainX(){
        return this.terrainX;
    }
    public float getTerrainY(){
        return this.terrainY;
    }
}

