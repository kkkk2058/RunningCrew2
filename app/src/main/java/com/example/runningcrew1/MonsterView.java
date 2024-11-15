package com.example.runningcrew1.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.example.runningcrew1.R;
import com.example.runningcrew1.models.MonsterModel;

public class MonsterView extends View {
    private MonsterModel monsterModel;
    private Bitmap monsterBitmap;

    public MonsterView(Context context, MonsterModel model) {
        super(context);
        this.monsterModel = model;

        // monster.png 이미지 로드 후 크기 조정 (예: 100x100 크기로 축소)
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.monster);
        int desiredWidth = 200; // 원하는 너비
        int desiredHeight = 200; // 원하는 높이
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
        invalidate(); // 화면을 지속적으로 갱신하여 몬스터가 움직임
    }
}
