package com.example.runningcrew1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // 상태를 READY로 설정
        StateManager.setCurrentState(StateManager.GameState.READY);

        Button btnEchoMode = findViewById(R.id.btnEchoMode);
        Button btnBattleMode = findViewById(R.id.btnBattleMode);

        // 1인 Echo 모드 클릭 시
        btnEchoMode.setOnClickListener(v -> {
            if (StateManager.getCurrentState() == StateManager.GameState.READY) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("mode", "echo");
                StateManager.setCurrentState(StateManager.GameState.RUNNING); // 게임 실행 상태로 변경
                startActivity(intent);
            }
        });

        // 2인 Battle 모드 클릭 시
        btnBattleMode.setOnClickListener(v -> {
            if (StateManager.getCurrentState() == StateManager.GameState.READY) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("mode", "battle");
                StateManager.setCurrentState(StateManager.GameState.RUNNING); // 게임 실행 상태로 변경
                startActivity(intent);
            }
        });
    }
}
