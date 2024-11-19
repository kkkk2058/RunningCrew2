package com.example.runningcrew1;

import java.util.Random;

public class ItemModel {

    private float displayWidth;
    private float displayHeight;

    private float itemX;
    private float itemY = 0;


    public ItemModel(int displayWidth,int displayHeight) {

        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.itemX = displayWidth;
        Random random = new Random();
        this.itemY = random.nextInt(displayHeight - 50);

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

    public void setItemY(float itemY) {
        this.itemY = itemY;
    }
}
