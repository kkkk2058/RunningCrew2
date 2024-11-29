package com.example.runningcrew1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class PlayerView extends View {
    private PlayerModel playerModel;
    private Bitmap playerBitmap;

    public PlayerView(Context context, PlayerModel model) {
        super(context);
        this.playerModel = model;

        // res/drawable/mario.jpg 이미지 로드 후 크기 조정
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mario);
        int desiredWidth = playerModel.getPlayerWidth();
        int desiredHeight = playerModel.getPlayerHeight();
        this.playerBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (playerBitmap != null) {
            float x = playerModel.getX() - (playerBitmap.getWidth() / 2);
            float y = playerModel.getY() - (playerBitmap.getHeight() / 2);
            canvas.drawBitmap(playerBitmap, x, y, new Paint());
        }
        invalidate();
    }
    public void updateView() {
        updateBitmap();
        invalidate();
    }

    private void updateBitmap() {
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mario);
        int desiredWidth = playerModel.getPlayerWidth();
        int desiredHeight = playerModel.getPlayerHeight();
        this.playerBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
    }
}
