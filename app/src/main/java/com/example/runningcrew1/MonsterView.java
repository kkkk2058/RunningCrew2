package com.example.runningcrew1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;


// 김세훈 (20201788)

public class MonsterView extends View {
    private MonsterModel monsterModel;
    private Bitmap monsterBitmap;

    public MonsterView(Context context, MonsterModel model) {
        super(context);
        this.monsterModel = model;


        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.monster);
        int desiredWidth = 200; // 몬스터 너비
        int desiredHeight = 200; // 몬스터 높이
        this.monsterBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (monsterModel.isActive() && monsterBitmap != null) {
            // 몬스터 이미지를 몬스터의 위치에 그리기
            float x = monsterModel.getX() - (monsterBitmap.getWidth() / 2);
            float y = monsterModel.getY() - (monsterBitmap.getHeight() / 2);
            canvas.drawBitmap(monsterBitmap, x, y, null);
        }
        invalidate();
    }
}

