package com.example.runningcrew1;

public class StateManager {
    public enum GameState {
        READY, RUNNING, PAUSED, GAME_OVER
    }

    private static GameState currentState = GameState.READY;

    public static GameState getCurrentState() {
        return currentState;
    }

    public static void setCurrentState(GameState newState) {
        currentState = newState;
    }
}

