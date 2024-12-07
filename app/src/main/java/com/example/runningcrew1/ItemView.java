package com.example.runningcrew1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class ItemView extends View {
    private ItemModel itemModel;
    private Bitmap itemBitmap;

    public ItemView(Context context, ItemModel model) {
        super(context);
        this.itemModel = model;

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.itembox);
        int desiredWidth = 200;
        int desiredHeight = 200;
        this.itemBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (itemBitmap != null) {
            float x = itemModel.getItemX() - (itemBitmap.getWidth() / 2);
            float y = itemModel.getItemY() - (itemBitmap.getHeight() / 2);
            canvas.drawBitmap(itemBitmap, x, y, null);
        }
        invalidate();
    }
}