package com.example.runningcrew1;

public class PlayerModel {
    private int score;
    private boolean isAlive;

    public PlayerModel() {
        this.score = 0;
        this.isAlive = true;
    }

    public int getScore() {
        return score;
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

    public void reset() {
        this.score = 0;
        this.isAlive = true;
    }
}

