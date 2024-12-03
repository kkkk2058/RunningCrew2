
package com.example.runningcrew1;

public class MapModel {

    private int displayWidth =0 ;
    private int displayHeight =0;

    private int terrainX;
    private int terrainY;
    private int width;


    public MapModel(int displayWidth, int randNum){

        this.displayWidth = displayWidth;

        this.terrainX = displayWidth-600;
        this.terrainY = randNum;
        this.width = width;
    }

    public void updatePosition(){
        this.terrainX -= 1;
//        Log.d("UpdatePosition", "Map updated: X = " + this.terrainX);

    }

    public int getTerrainX(){
        return this.terrainX;
    }
    public int getTerrainY(){
        return this.terrainY;
    }

    public int getWidth() {
        return width;
    }
}