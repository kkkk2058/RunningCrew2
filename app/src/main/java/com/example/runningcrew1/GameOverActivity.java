package com.example.runningcrew1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // 상태를 GAME_OVER로 설정
        StateManager.setCurrentState(StateManager.GameState.GAME_OVER);

        // 점수 가져오기
        int score = getIntent().getIntExtra("score", 0);

        // 점수 표시
        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("점수: " + score);

        // 시작 화면으로 돌아가는 버튼
        Button btnReturnToMenu = findViewById(R.id.btnReturnToMenu);
        btnReturnToMenu.setOnClickListener(v -> {
            // 상태를 READY로 설정
            StateManager.setCurrentState(StateManager.GameState.READY);

            Intent intent = new Intent(GameOverActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish(); // GameOverActivity 종료
        });
    }
}
