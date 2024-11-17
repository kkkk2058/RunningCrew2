package com.example.runningcrew1;


import java.util.Random;

public class MapModel {

    private float displayWidth =0;
    private float displayHeight =0;

    private float terrainX = 0;
    private float terrainY = 0;


    public MapModel(float displayWidth, float displayHeight){

        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;

        this.terrainX = displayWidth;

        Random random = new Random();
        this.terrainY = random.nextInt((int)this.displayHeight-50);

    }

    public void updateposition(){
        this.terrainX -= 1;

    }

    public float getTerrainX(){
        return this.terrainX;
    }
    public float getTerrainY(){
        return this.terrainY;
    }
}
