package com.example.runningcrew1;

// 김세훈 (20201788)

public class StateManager {
    public enum GameState {
        READY, RUNNING, PAUSED, GAME_OVER
    }

    private static GameState currentState = GameState.READY;

    public static GameState getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(GameState newState) {
        if (currentState == newState) {
            return; // 동일한 상태로 전환하려는 경우 무시
        }
        currentState = newState;
    }

}
