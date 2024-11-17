package com.example.runningcrew1;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private PlayerModel playerModel;
    private PlayerView playerView;
    private MonsterModel monsterModel;
    private MonsterView monsterView;
    private FrameLayout gameLayout;
    private Button pauseButton, btnLeft, btnRight, btnJump;

    private int screenWidth;
    private int screenHeight;

    private Handler moveHandler = new Handler(Looper.getMainLooper());
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면 크기 가져오기
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;

        // 플레이어와 몬스터 모델 생성
        playerModel = new PlayerModel(screenWidth / 2f, screenHeight - 500, screenWidth, screenHeight);
        monsterModel = new MonsterModel(screenWidth / 4f, screenHeight / 4f, 5, 5);

        // 플레이어와 몬스터 뷰 생성
        playerView = new PlayerView(this, playerModel);
        monsterView = new MonsterView(this, monsterModel);

        // FrameLayout으로 게임 화면 구성
        gameLayout = new FrameLayout(this);
        gameLayout.addView(monsterView);
        gameLayout.addView(playerView);

        // 버튼 생성 및 레이아웃 설정
        setupButtons();

        // 일시정지 버튼 추가
        setupPauseButton();

        setContentView(gameLayout);

        // 게임 루프 시작
        startGameLoop();
    }

    private void setupButtons() {
        // 버튼 생성
        btnLeft = new Button(this);
        btnLeft.setText("←");
        btnRight = new Button(this);
        btnRight.setText("→");
        btnJump = new Button(this);
        btnJump.setText("Jump");

        // 좌우 이동 버튼 터치 이벤트 설정
        btnLeft.setOnTouchListener((v, event) -> handleMove(event, true));
        btnRight.setOnTouchListener((v, event) -> handleMove(event, false));

        // 점프 버튼 클릭 이벤트 설정
        btnJump.setOnClickListener(v -> playerModel.jump());

        // 버튼 배치 설정
        FrameLayout.LayoutParams leftParams = new FrameLayout.LayoutParams(
                200, 200, Gravity.BOTTOM | Gravity.START
        );
        leftParams.leftMargin = screenWidth / 2 - 300;
        leftParams.bottomMargin = 50;

        FrameLayout.LayoutParams jumpParams = new FrameLayout.LayoutParams(
                200, 200, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
        );
        jumpParams.bottomMargin = 50;

        FrameLayout.LayoutParams rightParams = new FrameLayout.LayoutParams(
                200, 200, Gravity.BOTTOM | Gravity.END
        );
        rightParams.rightMargin = screenWidth / 2 - 300;
        rightParams.bottomMargin = 50;

        // 버튼 추가
        gameLayout.addView(btnLeft, leftParams);
        gameLayout.addView(btnJump, jumpParams);
        gameLayout.addView(btnRight, rightParams);
    }

    private void setupPauseButton() {
        pauseButton = new Button(this);
        pauseButton.setText("Pause");
        pauseButton.setOnClickListener(v -> pauseGame());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 50;
        params.leftMargin = 50;
        gameLayout.addView(pauseButton, params);
    }

    private void startGameLoop() {
        new Thread(() -> {
            while (playerModel.isAlive()) {
                runOnUiThread(() -> {
                    // 모델 업데이트
                    playerModel.updatePosition();
                    monsterModel.updatePosition();

                    // 충돌 체크
                    if (playerModel.checkCollision(monsterModel.getX(), monsterModel.getY())) {
                        playerModel.setAlive(false);
                        endGame();
                    }

                    // 점수 증가 로직
                    playerModel.increaseScore(1);

                    // 화면 업데이트
                    playerView.invalidate();
                });

                try {
                    Thread.sleep(16); // 약 60FPS
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

    private boolean handleMove(MotionEvent event, boolean isLeft) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isLeft) {
                    isMovingLeft = true;
                    startContinuousMove(true);
                } else {
                    isMovingRight = true;
                    startContinuousMove(false);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isLeft) {
                    isMovingLeft = false;
                } else {
                    isMovingRight = false;
                }
                break;
        }
        return true;
    }

    private void startContinuousMove(boolean isLeft) {
        moveHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLeft && isMovingLeft) {
                    playerModel.moveLeft();
                    moveHandler.postDelayed(this, 50);
                } else if (!isLeft && isMovingRight) {
                    playerModel.moveRight();
                    moveHandler.postDelayed(this, 50);
                }
            }
        }, 50);
    }
}
