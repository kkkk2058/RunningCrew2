package com.example.runningcrew1;

import java.util.Random;

public class ItemModel {

    private float displayWidth;
    private float displayHeight;

    private float itemX = 0;
    private float itemY = 0;


    public ItemModel(int displayWidth,int displayHeight, int terrainY) {

        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.itemX = displayWidth;

        Random random = new Random();
        int randNum;
        do {
            randNum = random.nextInt(displayHeight - 50);
        } while (terrainY == randNum);
        this.itemY = randNum;

    }

    public void updatePosition(){
        this.itemX -= 1;
    }

    public float getItemX(){
        return this.itemX;
    }
    public float getItemY(){
        return this.itemY;
    }
}