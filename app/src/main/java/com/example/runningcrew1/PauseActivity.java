package com.example.runningcrew1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PauseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        Button btnResume = findViewById(R.id.btnResume);
        Button btnReturnToMenu = findViewById(R.id.btnReturnToMenu);

        // "게임으로 돌아가기" 버튼 클릭 시
        btnResume.setOnClickListener(v -> {
            StateManager.setCurrentState(StateManager.GameState.RUNNING); // 상태 변경
            finish(); // PauseActivity 종료, GameActivity로 복귀
        });

        // "시작화면으로 돌아가기" 버튼 클릭 시
        btnReturnToMenu.setOnClickListener(v -> {
            StateManager.setCurrentState(StateManager.GameState.READY); // 상태 초기화
            Intent intent = new Intent(PauseActivity.this, MainMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // PauseActivity 종료
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        StateManager.setCurrentState(StateManager.GameState.PAUSED);
    }
}

