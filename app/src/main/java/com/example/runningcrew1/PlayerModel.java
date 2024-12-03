
package com.example.runningcrew1;

import android.util.Log;

public class PlayerModel {
    private float x, y; // 플레이어 위치
    private float speed; // 플레이어 이동 속도
    private boolean isAlive; // 생존 상태
    private int score; // 점수
    private float screenWidth, screenHeight; // 화면 크기
    private boolean isJumping; // 점프 상태
    private float jumpVelocity; // 점프 속도
    private static final float GRAVITY = 2.5f; // 중력 가속도
    private static final float TOP_LIMIT = 100; // 화면 상단의 제한 (100px 여유)
    private final float attackRange = 200f; // 공격 범위 설정

    private float size = 1;
    private int playerWidth = 200;
    private int playerHeight = 200;
    public PlayerModel(float startX, float startY, float screenWidth, float screenHeight) {
        this.x = startX;
        this.y = startY;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.speed = 15; // 기본 속도
        this.isAlive = true;
        this.score = 0;
        this.isJumping = false;
        this.jumpVelocity = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void moveLeft() {
        x -= speed;
        if (x < 0) x = 0;
        Log.d("PlayerPosition", "Moved Left: X = " + x + ", Y = " + y);
    }

    public void moveRight() {
        x += speed;
        if (x > screenWidth) x = screenWidth;
        Log.d("PlayerPosition", "Moved Right: X = " + x + ", Y = " + y);

    }

    public void jump() {
        if (!isJumping || y > TOP_LIMIT) {
            isJumping = true;
            jumpVelocity = -30; // 점프 초기 속도
        }
    }

    public void updatePosition() {
        if (isJumping) {
            y += jumpVelocity; // 점프 이동
            jumpVelocity += GRAVITY; // 중력 효과

            // 화면 상단 제한
            if (y < TOP_LIMIT) {
                y = TOP_LIMIT;
                jumpVelocity = 0; // 상단에 도달하면 점프 정지
            }

            // 땅에 닿으면 점프 종료
            if (y >= screenHeight - 400) {
                y = screenHeight - 400;
                isJumping = false;
            }
        }

        // 화면을 벗어나지 않도록 제한
        if (y > screenHeight) {
            setAlive(false); // 플레이어가 화면 아래로 떨어지면 죽음
        }
    }

    public void increaseScore(int points) {
        this.score += points;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getScore() {
        return score;
    }

    public int getPlayerWidth(){
        return playerWidth;
    }
    public int getPlayerHeight(){
        return playerHeight;
    }
    public float getPlayerSpeed() {
        return speed;
    }
    public void setPlayerWidth(int width) {
        this.playerWidth = width;
    }
    public void setPlayerHeight(int height) {
        this.playerHeight = height;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean checkCollision(float monsterX, float monsterY) {
        float distance = (float) Math.sqrt(Math.pow(x - monsterX, 2) + Math.pow(y - monsterY, 2));
        return distance < 100; // 임의로 100을 충돌 거리로 설정
    }
    public void applyEffect(ItemModel.ItemType type) {
        switch (type) {
            case GROW:
                this.playerHeight *= 2;
                this.playerWidth *= 2;
                break;
            case SHRINK:
                this.playerHeight *= 0.5;
                this.playerWidth *= 0.5;
                break;
            case SPEEDUP:
                this.speed *= 1.5;
                break;
        }
    }
    public boolean checkItemCollision(float itemX, float itemY) {
        //return this.x < itemX + 50 && this.x + this.playerWidth > itemX &&
        //        this.y < itemY + 50 && this.y + this.playerHeight > itemY;
        float distance = (float) Math.sqrt(Math.pow(x - itemX, 2) + Math.pow(y - itemY, 2));
        return distance < 100; // 임의로 100을 충돌 거리로 설정
    }

    public float getAttackRange() {
        return attackRange;
    }

    public boolean isInAttackRange(float monsterX, float monsterY) {
        float distance = (float) Math.sqrt(Math.pow(monsterX - x, 2) + Math.pow(monsterY - y, 2));
        return distance <= attackRange;
    }
}