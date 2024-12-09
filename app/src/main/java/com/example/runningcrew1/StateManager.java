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
        if (currentState == newState) {
            return; // 동일한 상태로 전환하려는 경우 무시
        }
        logStateChange(currentState, newState); // 상태 전환 로그 출력
        currentState = newState;
    }

    private static void logStateChange(GameState oldState, GameState newState) {
        System.out.println("State changed from " + oldState + " to " + newState);
    }

//    public static boolean isRunning() {
//        return currentState == GameState.RUNNING;
//    }
//
//    public static boolean isPaused() {
//        return currentState == GameState.PAUSED;
//    }
//
//    public static boolean isGameOver() {
//        return currentState == GameState.GAME_OVER;
//    }
}
