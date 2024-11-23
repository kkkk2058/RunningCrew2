package com.example.runningcrew1;

public class GroundMonsterModel {
    private float x, y;  // 몬스터의 위치
    private float speedX;  // 몬스터의 가로 방향 속도
    private int groundHeight; // 지면 높이
    private boolean isActive;  // 몬스터 활성화 여부




    public GroundMonsterModel(float startX, int groundHeight, float speedX) {
        this.x = startX;
        this.groundHeight = groundHeight;
        this.speedX = speedX;
        this.y = groundHeight; // y 좌표는 항상 지면 높이에 고정
        this.isActive = true;
    }

    public void updatePosition() {
        if (!isActive) return;

        x += speedX;

        // 화면 경계 체크 (가로 방향만 확인)
        if (x < 0 || x > 1080) { // 화면 너비 기준
            speedX = -speedX; // 방향 반전
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
