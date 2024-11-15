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

        int score = getIntent().getIntExtra("score", 0);

        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("점수: " + score);

        Button btnReturnToMenu = findViewById(R.id.btnReturnToMenu);

        btnReturnToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
