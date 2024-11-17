package com.example.runningcrew1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class ItemView extends View {
    private ItemModel itemModel;
    private Bitmap mapBitmap;

    public ItemView(Context context, ItemModel model) {
        super(context);
        this.itemModel = model;


        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.itembox);
        int desiredWidth = 100; // 원하는 너비
        int desiredHeight = 100; // 원하는 높이
        this.mapBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mapBitmap != null) {
            float x = itemModel.getItemX() - (mapBitmap.getWidth() / 2);
            float y = itemModel.getItemY() - (mapBitmap.getHeight() / 2);
            canvas.drawBitmap(mapBitmap, x, y, null);
        }
        invalidate();
    }
}
