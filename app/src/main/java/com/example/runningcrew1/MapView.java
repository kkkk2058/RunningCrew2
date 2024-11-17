package com.example.runningcrew1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class MapView extends View {
    private MapModel mapModel;
    private Bitmap mapBitmap;

    public MapView(Context context, MapModel model) {
        super(context);
        this.mapModel = model;


        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
        int desiredWidth = 100; // 원하는 너비
        int desiredHeight = 100; // 원하는 높이
        this.mapBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mapBitmap != null) {
            float x = mapModel.getTerrainX() - (mapBitmap.getWidth() / 2);
            float y = mapModel.getTerrainY() - (mapBitmap.getHeight() / 2);
            canvas.drawBitmap(mapBitmap, x, y, null);
        }
        invalidate();
    }
}
