package com.example.runningcrew1;

import android.util.Log;

import java.util.Random;

public class ItemModel {
    public enum ItemType {
        GROW,
        SHRINK,
        SPEEDUP
    }
    private ItemType type;

    private float displayWidth;
    private float displayHeight;

    private int itemX;
    private int itemY = 0;


    public ItemModel(int displayWidth,int displayHeight) {

        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.itemX = displayWidth;
        Random random = new Random();
        this.itemY = random.nextInt(displayHeight - 50);
        this.type = generateRandomType();

    }

    public void updatePosition(){
        this.itemX -= 1;
        Log.d("UpdatePosition", "Item updated: X = " + this.itemX);

    }

    public float getItemX(){
        return this.itemX;
    }
    public float getItemY(){
        return this.itemY;
    }

    public void setItemY(int itemY) {
        this.itemY = itemY;
    }

    private ItemType generateRandomType() {
        int rand = new Random().nextInt(3);
        switch (rand) {
            case 0:
                return ItemType.GROW;
            case 1:
                return ItemType.SHRINK;
            case 2:
                return ItemType.SPEEDUP;
            default:
                return ItemType.GROW;
        }
    }

    public ItemType getType() {
        return type;
    }

    public int getX(){
        return itemX;
    }
    public int getY(){
        return itemY;
    }

}