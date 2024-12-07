package com.example.runningcrew1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class MapView extends View {
    private MapModel mapModel;
    private Bitmap mapBitmap;
    private int randNum;

    public MapView(Context context, MapModel model, int randNum) {
        super(context);
        this.mapModel = model;
        this.randNum = randNum;


        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
        int desiredWidth = 100;
        int desiredHeight = 100;
        this.mapBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mapBitmap != null) {
            for (int i = 0; i < randNum; i++) {
                float x = mapModel.getTerrainX() - (mapBitmap.getWidth() / 2) + (i * mapBitmap.getWidth());
                float y = mapModel.getTerrainY() - (mapBitmap.getHeight() / 2);

                canvas.drawBitmap(mapBitmap, x, y, null);
            }
        }
        invalidate();
    }
}