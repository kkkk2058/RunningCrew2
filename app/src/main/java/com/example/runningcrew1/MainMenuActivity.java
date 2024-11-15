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

        Button btnEchoMode = findViewById(R.id.btnEchoMode);
        Button btnBattleMode = findViewById(R.id.btnBattleMode);

        // 1인 Echo 모드 클릭 시
        btnEchoMode.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            intent.putExtra("mode", "echo");
            startActivity(intent);
        });

        // 2인 Battle 모드 클릭 시
        btnBattleMode.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            intent.putExtra("mode", "battle");
            startActivity(intent);
        });
    }
}

