package com.example.runningcrew1;

public class MapModel {

    private int displayWidth =0;
    private int displayHeight =0;

    private int terrainX = 0;
    private int terrainY = 0;


    public MapModel(int displayWidth, int randNum){

        this.displayWidth = displayWidth;

        this.terrainX = displayWidth;
        this.terrainY = randNum;
    }

    public void updatePosition(){
        this.terrainX -= 1;
    }

    public int getTerrainX(){
        return this.terrainX;
    }
    public int getTerrainY(){
        return this.terrainY;
    }
}

