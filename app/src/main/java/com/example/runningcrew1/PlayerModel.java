package com.example.runningcrew1;

public class PlayerModel {
    private float x, y; // 플레이어 위치
    private float speed; // 플레이어 이동 속도
    private boolean isAlive; // 생존 상태
    private int score; // 점수
    private float screenWidth, screenHeight; // 화면 크기
    private boolean isJumping; // 점프 상태
    private float jumpVelocity; // 점프 속도
    private static final float GRAVITY = 1.5f; // 중력 가속도

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
    }

    public void moveRight() {
        x += speed;
        if (x > screenWidth) x = screenWidth;
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            jumpVelocity = -30; // 점프 초기 속도
        }
    }

    public void updatePosition() {
        if (isJumping) {
            y += jumpVelocity; // 점프 이동
            jumpVelocity += GRAVITY; // 중력 효과

            // 땅에 닿으면 점프 종료
            if (y >= screenHeight - 500) {
                y = screenHeight - 500;
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

    public boolean checkCollision(float monsterX, float monsterY) {
        float distance = (float) Math.sqrt(Math.pow(x - monsterX, 2) + Math.pow(y - monsterY, 2));
        return distance < 100; // 임의로 100을 충돌 거리로 설정
    }
}
