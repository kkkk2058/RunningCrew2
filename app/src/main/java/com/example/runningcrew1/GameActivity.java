package com.example.runningcrew1;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private PlayerModel playerModel;
    private PlayerView playerView;
    private MonsterModel monsterModel;
    private MonsterView monsterView;


    private FrameLayout gameView;
    private ImageView groundView;
    private LinearLayout controllerLayout;

    private Button btnLeft, btnRight, btnJump, btnPause, btnAttack;

    private int screenWidth;
    private int screenHeight;


    private Handler moveHandler = new Handler(Looper.getMainLooper());
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;

    private Handler mapHandler = new Handler(Looper.getMainLooper());

    private List<MapModel> mapModels = new ArrayList<>();
    private List<MapView> mapViews = new ArrayList<>();


    private List<ItemModel> itemModels = new ArrayList<>();
    private List<ItemView> itemViews = new ArrayList<>();
    private List<MonsterModel> monsterModels = new ArrayList<>();
    private List<MonsterView> monsterViews = new ArrayList<>();
    private int monsterSpawnInterval = 300; // 몬스터 생성 간격 (프레임 단위)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 기존 코드 유지
        setContentView(R.layout.activity_game);

        // 화면 크기 가져오기
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;

        // XML 뷰 찾기
        gameView = findViewById(R.id.gameView);
        groundView = findViewById(R.id.groundView);
        controllerLayout = findViewById(R.id.controllerLayout);

        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnJump = findViewById(R.id.btnJump);
        btnPause = findViewById(R.id.btnPause);
        btnAttack = findViewById(R.id.btnAttack);

        View mainView = findViewById(android.R.id.content);
        mainView.setFocusableInTouchMode(true);
        mainView.requestFocus();
        mainView.setOnKeyListener(this::handleKeyboardInput);


        // 플레이어 초기화
        playerModel = new PlayerModel(screenWidth / 2f, screenHeight - 400, screenWidth, screenHeight);
        playerView = new PlayerView(this, playerModel);
        gameView.addView(playerView);


        setupButtonListeners();

        StateManager.setCurrentState(StateManager.GameState.RUNNING);
        // 게임 루프 시작
        startGameLoop();
    }



    private void setupButtonListeners() {
        // 좌우 이동 버튼 터치 이벤트 설정
        btnLeft.setOnTouchListener((v, event) -> handleMove(event, true));
        btnRight.setOnTouchListener((v, event) -> handleMove(event, false));

        // 점프 버튼 클릭 이벤트 설정
        btnJump.setOnClickListener(v -> playerModel.jump());

        btnPause.setOnClickListener(v -> pauseGame());

        //공격 버튼 클릭 이벤트 설정
        btnAttack.setOnClickListener(v -> {
            attackMonsters();
            playerModel.startAttack();
        });
    }

    private boolean handleKeyboardInput(View v, int keyCode, KeyEvent event) {
        if (StateManager.getCurrentState() != StateManager.GameState.RUNNING) return false;

        // 키 입력 이벤트 처리
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                // 연속 이동 동작 시작
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_A) {
                    if (!isMovingLeft) { // 이미 이동 중이 아니면 kkd시작
                        isMovingLeft = true;
                        startContinuousMove(true);
                    }
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_D) {
                    if (!isMovingRight) { // 이미 이동 중이 아니면 시작
                        isMovingRight = true;
                        startContinuousMove(false);
                    }
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_SPACE || keyCode == KeyEvent.KEYCODE_W) {
                    playerModel.jump();
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_K) {
                    attackMonsters();
                    return true;
                }
                break;

            case KeyEvent.ACTION_UP:
                // 연속 이동 동작 중지
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_A) {
                    isMovingLeft = false; // 이동 중지
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_D) {
                    isMovingRight = false; // 이동 중지
                    return true;
                }
                break;
        }
        return false;
    }


    private void startGameLoop() {
        new Thread(() -> {
            int frameCounter = 0;
            int newMapCounter = 120;
            int newItemCounter = 120;
            int monsterSpawnInterval = 300; // 몬스터 생성 간격 (프레임 단위)

            while (playerModel.isAlive()) {
                // 현재 상태를 확인
                if (StateManager.getCurrentState() != StateManager.GameState.RUNNING) {
                    try {
                        Thread.sleep(100); // RUNNING 상태가 아닐 경우 대기
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                frameCounter++;

                // 맵 생성
                if (frameCounter % newMapCounter == 0) {
                    runOnUiThread(this::startMapGeneration);
                }

                // 아이템 생성
                if (frameCounter % newItemCounter == 0) {
                    runOnUiThread(this::startItemGeneration);
                }

                // 몬스터 생성
                if (frameCounter % monsterSpawnInterval == 0) {
                    runOnUiThread(this::spawnMonster);
                }

                runOnUiThread(() -> {
                    // 모델 업데이트
                    playerModel.updateAttackState();
                    playerModel.updatePosition();

                    // 모든 몬스터 업데이트
                    for (int i = monsterModels.size() - 1; i >= 0; i--) {
                        MonsterModel monster = monsterModels.get(i);
                        monster.updatePosition();

                        // 화면 밖으로 나간 몬스터 제거
                        if (monster.getX() + 100 < 0) {
                            gameView.removeView(monsterViews.get(i));
                            monsterModels.remove(i);
                            monsterViews.remove(i);
                        }
                    }

                    startMapMovement();
                    startItemMovement();

                    // 아이템 충돌 체크
                    checkItemCollision();

                    // 몬스터 충돌 체크
                    checkMonsterCollision();

                    // 점수 증가
                    playerModel.increaseScore(1);

                    // 화면 갱신
                    playerView.invalidate();

                    for (MonsterView monsterView : monsterViews) {
                        monsterView.invalidate();
                    }
                });

                try {
                    Thread.sleep(32); // 약 60FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    private void pauseGame() {
        // 게임 상태를 PAUSED로 변경
        StateManager.setCurrentState(StateManager.GameState.PAUSED);

        Intent intent = new Intent(GameActivity.this, PauseActivity.class);
        startActivity(intent);
    }


    private void endGame() {
        // 게임 상태를 GAME_OVER로 변경
        StateManager.setCurrentState(StateManager.GameState.GAME_OVER);

        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra("score", playerModel.getScore());
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        StateManager.setCurrentState(StateManager.GameState.RUNNING); // 상태를 RUNNING으로 설정
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
                if (StateManager.getCurrentState() != StateManager.GameState.RUNNING) return;

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

    private void startMapGeneration() {

        if (StateManager.getCurrentState() != StateManager.GameState.RUNNING) return;

        if (!playerModel.isAlive())
            return;

        Random random = new Random();
        int randHeight = random.nextInt(screenHeight) + 50;
        int randNum = random.nextInt(5) + 1;


        MapModel newMapModel = new MapModel(screenWidth, randHeight);
        MapView newMapView = new MapView(GameActivity.this, newMapModel, randNum);

        Log.d("GameActivity", "생성된 Map X값: " + newMapModel.getTerrainX() + ",랜덤num " + randNum);

        mapModels.add(newMapModel);
        mapViews.add(newMapView);

        gameView.addView(newMapView);
    }

    private void startMapMovement() {
        // RUNNING 상태가 아니라면 맵 움직임 중지
        if (StateManager.getCurrentState() != StateManager.GameState.RUNNING) return;

        for (int i = mapModels.size() - 1; i >= 0; i--) {
            MapModel mapModel = mapModels.get(i);
            mapModel.updatePosition();

            if (mapModel.getTerrainX() + 1000 < 0) {
                Log.d("GameActivity", "맵 제거됨: X = " + mapModel.getTerrainX());
                mapModels.remove(i);
                gameView.removeView(mapViews.get(i));
                mapViews.remove(i);
            } else {
                mapViews.get(i).setX(mapModel.getTerrainX());
            }
        }
    }


    private void startItemGeneration() {

        Log.d("GameActivity", "아이템 생성 호출됨 ");
        if (StateManager.getCurrentState() != StateManager.GameState.RUNNING) return;

        if (!playerModel.isAlive())
            return;

        ItemModel newItemModel = new ItemModel(screenWidth, screenHeight);
        ItemView newItemView = new ItemView(GameActivity.this, newItemModel);

        if (!mapModels.isEmpty()) {
            MapModel lastMap = mapModels.get(mapModels.size() - 1);
            if (Math.abs(lastMap.getTerrainY() - newItemModel.getItemY()) < 10) {
                newItemModel.setItemY((int) (newItemModel.getItemY() - 30));
            }
        }


        itemModels.add(newItemModel);
        itemViews.add(newItemView);
        gameView.addView(newItemView);

    }

    private void startItemMovement() {
        // RUNNING 상태가 아니라면 아이템 움직임 중지
        if (StateManager.getCurrentState() != StateManager.GameState.RUNNING) return;

        for (int i = itemModels.size() - 1; i >= 0; i--) {
            ItemModel itemModel = itemModels.get(i);
            itemModel.updatePosition();

            if (itemModel.getItemX() + 50 < 0) {
                itemModels.remove(i);
                gameView.removeView(itemViews.get(i));
                itemViews.remove(i);
            } else {
                ItemView itemView = itemViews.get(i);
                itemView.setX(itemModel.getItemX());
                itemView.invalidate();
            }
        }
    }


    private void checkItemCollision() {
        for (int i = 0; i < itemModels.size(); i++) {
            ItemModel item = itemModels.get(i);

            if (playerModel.checkItemCollision(item.getItemX(), item.getItemY())) { // 50은 아이템 크기
                playerModel.applyEffect(item.getType());
                playerView.updateView();

                restorePlayerEffectAfterDelay(item.getType());

                //gameView.removeView(itemViews.get(i));
                int finalI = i;
                runOnUiThread(() -> {
                    gameView.removeView(itemViews.get(finalI));
                });
                itemModels.remove(i);
                itemViews.remove(i);
                i--;

                Log.d("GameActivity", "아이템 효과 적용됨: " + item.getType());

            }
        }
    }

    private void spawnMonster() {
        Random random = new Random();
        float spawnX = screenWidth;
        float spawnY = random.nextInt(screenHeight / 2) + screenHeight / 4f;
        MonsterModel newMonster = new MonsterModel(spawnX, spawnY, 1, 1);
        MonsterView newMonsterView = new MonsterView(this, newMonster);

        monsterModels.add(newMonster);
        monsterViews.add(newMonsterView);
        gameView.addView(newMonsterView);
    }

    private void checkMonsterCollision() {
        for (int i = monsterModels.size() - 1; i >= 0; i--) {
            MonsterModel monster = monsterModels.get(i);

            if (playerModel.checkCollision(monster.getX(), monster.getY())) {
                playerModel.setAlive(false);
                endGame();
                break;
            }
        }
    }


    private void restorePlayerEffectAfterDelay(ItemModel.ItemType type) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            switch (type) {
                case GROW:
                    playerModel.setPlayerWidth(playerModel.getPlayerWidth()/2);
                    playerModel.setPlayerHeight(playerModel.getPlayerHeight()/2);
                    break;
                case SHRINK:
                    playerModel.setPlayerWidth(playerModel.getPlayerWidth()*2);
                    playerModel.setPlayerHeight(playerModel.getPlayerHeight()*2);
                    break;
                case SPEEDUP:
                    playerModel.setSpeed(playerModel.getPlayerSpeed()/2);
                    break;
            }
            playerView.updateView();
        }, 3000);
    }



    private void attackMonsters() {
        for (int i = monsterModels.size() - 1; i >= 0; i--) {
            MonsterModel monster = monsterModels.get(i);

            // 플레이어의 공격 범위 내에 있는지 확인
            if (monster.isActive() && playerModel.isInAttackRange(monster.getX(), monster.getY())) {
                monster.deactivate(); // 몬스터 비활성화
                int finalI = i; // 람다 표현식에서 사용할 인덱스

                runOnUiThread(() -> gameView.removeView(monsterViews.get(finalI))); // 뷰에서 제거

                monsterModels.remove(i); // 모델 리스트에서 제거
                monsterViews.remove(i); // 뷰 리스트에서 제거
            }
        }
    }

}