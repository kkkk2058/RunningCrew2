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
        int desiredWidth = 200; // 원하는 너비
        int desiredHeight = 200; // 원하는 높이
        this.playerBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (playerBitmap != null) {
            // 플레이어 이미지 그리기
            float x = playerModel.getX() - (playerBitmap.getWidth() / 2);
            float y = playerModel.getY() - (playerBitmap.getHeight() / 2);
            canvas.drawBitmap(playerBitmap, x, y, new Paint());
        }
        invalidate(); // 지속적으로 화면 갱신
    }
}
