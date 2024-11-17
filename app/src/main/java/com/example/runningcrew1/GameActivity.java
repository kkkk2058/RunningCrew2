package com.example.runningcrew1;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private PlayerModel playerModel;
    private MonsterModel monsterModel;
    private Button btnLeft, btnRight, btnJump, pauseButton;
    private FrameLayout gameView;

    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // 화면 크기 가져오기
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;

        // XML 뷰 참조
        gameView = findViewById(R.id.gameView);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnJump = findViewById(R.id.btnJump);
        pauseButton = findViewById(R.id.pauseButton);

        // 모델 생성
        playerModel = new PlayerModel(screenWidth / 2f, screenHeight - 500, screenWidth, screenHeight);
        monsterModel = new MonsterModel(screenWidth / 4f, screenHeight / 4f, 5, 5);

        // 버튼 이벤트
        btnLeft.setOnClickListener(v -> playerModel.moveLeft());
        btnRight.setOnClickListener(v -> playerModel.moveRight());
        btnJump.setOnClickListener(v -> playerModel.jump());
        pauseButton.setOnClickListener(v -> pauseGame());

        // 동적으로 PlayerView와 MonsterView 추가
        addGameElements();

        // 게임 루프 시작
        startGameLoop();
    }

    private void addGameElements() {
        PlayerView playerView = new PlayerView(this, playerModel);
        MonsterView monsterView = new MonsterView(this, monsterModel);
        gameView.addView(playerView);
        gameView.addView(monsterView);
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (playerModel.isAlive()) {
                runOnUiThread(() -> {
                    playerModel.updatePosition();
                    monsterModel.updatePosition();
                    if (playerModel.checkCollision(monsterModel.getX(), monsterModel.getY())) {
                        playerModel.setAlive(false);
                        endGame();
                    }
                });
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void pauseGame() {
        Intent intent = new Intent(GameActivity.this, PauseActivity.class);
        startActivity(intent);
    }

    private void endGame() {
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra("score", playerModel.getScore());
        startActivity(intent);
        finish();
    }
}
