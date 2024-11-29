package com.example.runningcrew1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class GroundMonsterView extends View {
    private GroundMonsterModel groundMonsterModel;
    private Bitmap groundMonsterBitmap;

    public GroundMonsterView(Context context, GroundMonsterModel model) {
        super(context);

        this.groundMonsterModel = model;

        try {
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.monster);
            int desiredWidth = 150; // 원하는 너비
            int desiredHeight = 150; // 원하는 높이
            this.groundMonsterBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
        } catch (Exception e) {
            e.printStackTrace();
            this.groundMonsterBitmap = null; // 기본값 처리
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (groundMonsterModel.isActive() && groundMonsterBitmap != null) {
            // 지면 몬스터 이미지를 몬스터의 위치에 그리기
            float x = groundMonsterModel.getX() - (groundMonsterBitmap.getWidth() / 2);
            float y = groundMonsterModel.getY() - (groundMonsterBitmap.getHeight() / 2);
            canvas.drawBitmap(groundMonsterBitmap, x, y, null);
        }
        invalidate(); // 화면을 지속적으로 갱신하여 몬스터가 움직임
    }
}
