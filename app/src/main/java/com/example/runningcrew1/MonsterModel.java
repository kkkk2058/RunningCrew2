package com.example.runningcrew1;

// 김세훈 (20201788)

public class MonsterModel {
    private float x, y;  // 몬스터 위치
    private float speedX, speedY;  // 몬스터 속도
    private boolean isActive;  // 몬스터 활성화 여부

    public MonsterModel(float startX, float startY, float speedX, float speedY) {
        this.x = startX;
        this.y = startY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.isActive = true;
    }

    public void updatePosition() {
        if (!isActive) return;

        x += speedX;
        y += speedY;

        if (x < 0 || x > 1080) {
            speedX = -speedX;
        }
        if (y < 0 || y > 1920) {
            speedY = -speedY;
        }


    }

    public void deactivate() {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}