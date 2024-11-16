package com.example.runningcrew1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runningcrew1.models.MonsterModel;
import com.example.runningcrew1.views.MonsterView;

public class GameActivity extends AppCompatActivity {

    private MonsterModel monsterModel;
    private MonsterView monsterView;
    private Handler handler = new Handler();
    private Runnable gameLoopRunnable;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // 몬스터 초기화
        monsterModel = new MonsterModel(500, 500, 5, 7);
        monsterView = new MonsterView(this, monsterModel);

        // gameView에 몬스터 추가
        FrameLayout gameLayout = findViewById(R.id.gameView);
        gameLayout.addView(monsterView);


        // 일시정지 버튼 설정
        Button btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(v -> {
            isPaused = true;
            handler.removeCallbacks(gameLoopRunnable); // 게임 루프 일시정지
            Intent intent = new Intent(GameActivity.this, PauseActivity.class);
            startActivity(intent);
        });

        // 게임 루프 초기화
        gameLoopRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isPaused) {
                    // 몬스터 위치 업데이트 및 뷰 갱신
                    monsterModel.updatePosition();
                    monsterView.invalidate();
                }
                // 루프 반복 (60FPS로 호출)
                handler.postDelayed(this, 16);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false; // 액티비티가 다시 활성화되면 게임 루프 재개
        handler.post(gameLoopRunnable); // 게임 루프 다시 시작
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true; // 액티비티가 일시정지되면 게임 루프 일시정지
        handler.removeCallbacks(gameLoopRunnable); // 루프 중지
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(gameLoopRunnable); // 루프 완전 종료
    }
}
