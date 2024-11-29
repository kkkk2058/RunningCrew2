package com.example.runningcrew1;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
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

    private Handler gameHandler = new Handler(Looper.getMainLooper());
    private boolean isGamePaused = false;

    private Handler moveHandler = new Handler(Looper.getMainLooper());
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;

    private Handler mapHandler = new Handler(Looper.getMainLooper());
    private Handler movementHandler = new Handler(Looper.getMainLooper());
    private List<MapModel> mapModels = new ArrayList<>();
    private List<MapView> mapViews = new ArrayList<>();

    private Handler itemHandler = new Handler(Looper.getMainLooper());
    private Handler itemMovementHandler = new Handler(Looper.getMainLooper());
    private List<ItemModel> itemModels = new ArrayList<>();
    private List<ItemView> itemViews = new ArrayList<>();
    private GroundMonsterModel groundMonsterModel;
    private GroundMonsterView groundMonsterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // XML 레이아웃 설정
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

        // 플레이어와 몬스터 맵 모델 생성d

        playerModel = new PlayerModel(screenWidth / 2f, screenHeight - 500, screenWidth, screenHeight);
        monsterModel = new MonsterModel(screenWidth / 4f, screenHeight / 4f, 0, 1);

        // 플레이어와 몬스터 맵 뷰 생성
        playerView = new PlayerView(this, playerModel);
        monsterView = new MonsterView(this, monsterModel);
        //roundMonsterView= new MonsterView(this, groundMonsterModel);


        // groundView 높이 계산 및 땅 몬스터 초기화
        groundView.post(() -> {
            int groundHeight = groundView.getHeight() + 150;
            setupGroundMonster(groundHeight);
        });

        // 게임 루프 시작은 setupGroundMonster가 호출된 이후 실행되도록 보장 / 미정
        new Handler(Looper.getMainLooper()).postDelayed(() -> startGameLoop(), 100);

        // Pause 버튼 클릭 이벤트 설정
        btnPause.setOnClickListener(v -> pauseGame());

        // 게임 화면에 뷰 추가
        gameView.addView(monsterView);
        gameView.addView(playerView);

        // 버튼 동작 설정
        setupButtonListeners();


        // 게임 루프 시작
        startGameLoop();
    }


    private void setupGroundMonster(int groundHeight) {
        // 땅 몬스터의 초기 X 위치를 동적으로 설정
        float startX = screenWidth;
        float speedX = 1; // 이동 속도
        groundMonsterModel = new GroundMonsterModel(startX, screenHeight - groundHeight, speedX);

        // 땅 몬스터 뷰 생성
        groundMonsterView = new GroundMonsterView(this, groundMonsterModel);

        // 게임 뷰에 추가
        gameView.addView(groundMonsterView);
    }

    private void setupButtonListeners() {
        // 좌우 이동 버튼 터치 이벤트 설정
        btnLeft.setOnTouchListener((v, event) -> handleMove(event, true));
        btnRight.setOnTouchListener((v, event) -> handleMove(event, false));

        // 점프 버튼 클릭 이벤트 설정
        btnJump.setOnClickListener(v -> playerModel.jump());

        //공격 버튼 클릭 이벤트 설정
        btnAttack.setOnClickListener(v -> attackMonsters());
    }

    private void startGameLoop() {
        new Thread(() -> {
            int frameCounter = 0;
            int newMapCounter = 120;
            int newItemCounter = 120;

            //runOnUiThread(this::startMapGeneration);
            //runOnUiThread(this::startItemGeneration);


            while (playerModel.isAlive()) {
                frameCounter++;

                if (frameCounter % newMapCounter == 0) {
                    runOnUiThread(this::startMapGeneration);
                }

                if (frameCounter % newItemCounter == 0) {
                    runOnUiThread(this::startItemGeneration);
                }

                runOnUiThread(() -> {

                    // 모델 업데이트
                    playerModel.updatePosition();
                    monsterModel.updatePosition();

                    startMapMovement();
                    startItemMovement();

                    checkItemCollision();

                    // 충돌 체크
                    if (playerModel.checkCollision(monsterModel.getX(), monsterModel.getY())) {
                        playerModel.setAlive(false);
                        endGame();
                    }

                    // 점수 증가 로직
                    playerModel.increaseScore(1);

                    playerView.invalidate();
                });
                if (!isGamePaused) {
                    runOnUiThread(() -> {
                        // 기존 모델 업데이트
                        playerModel.updatePosition();
                        monsterModel.updatePosition();


                        // 땅 몬스터 업데이트
                        if (groundMonsterModel != null) { // null 체크
                            groundMonsterModel.updatePosition();
                        }

                        // 충돌 체크 및 점수 업데이트
                        if (playerModel.checkCollision(monsterModel.getX(), monsterModel.getY())) {
                            playerModel.setAlive(false);
                            endGame();
                        }

                        playerModel.increaseScore(1);

                        // 화면 갱신
                        playerView.invalidate();
                        monsterView.invalidate();
                        if (groundMonsterView != null) {
                            groundMonsterView.invalidate();
                        }
                    });
                }

                try {
                    Thread.sleep(32); // 약 60FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void pauseGame() {
        isGamePaused = true; // 게임 중지
        Intent intent = new Intent(GameActivity.this, PauseActivity.class);
        startActivity(intent);
    }

    private void endGame() {
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra("score", playerModel.getScore());
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        isGamePaused = false; // 게임 재개
    }

    @Override
    protected void onPause() {
        super.onPause();
        isGamePaused = true; // 게임 중지
    }

    private boolean handleMove(MotionEvent event, boolean isLeft) {
        if (isGamePaused) return false; // 일시정지 상태에서는 무시

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
                if (isGamePaused) return; // 게임이 멈춘 상태에서는 동작 무시

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

        if (!playerModel.isAlive())
            return;

        Random random = new Random();
        int randHeight = random.nextInt(screenHeight) + 50;
        int randNum = random.nextInt(5) + 1;


        MapModel newMapModel = new MapModel(screenWidth, randHeight);
        MapView newMapView = new MapView(GameActivity.this, newMapModel, randNum);

        Log.d("GameActivity", "생성된 Map X값: " + newMapModel.getTerrainX() + ", 화면 가로 크기: " + screenWidth + "세로 크기:" + screenHeight);

        mapModels.add(newMapModel);
        mapViews.add(newMapView);

        gameView.addView(newMapView);
    }

    private void startMapMovement() {

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
            //mapViews.get(i).setX(mapModel.getTerrainX());
        }
    }

    private void startItemGeneration() {
        Log.d("GameActivity", "아이템 생성 호출됨 ");

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

                gameView.removeView(itemViews.get(i));
                itemModels.remove(i);
                itemViews.remove(i);
                i--;

                Log.d("GameActivity", "아이템 효과 적용됨: " + item.getType());

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
        // 공중 몬스터 제거
        for (int i = 0; i < mapModels.size(); i++) {
            MonsterModel monster = monsterModel;
            if (playerModel.isInAttackRange(monster.getX(), monster.getY())) {
                gameView.removeView(monsterView);
                mapModels.remove(i);
                i--; // 리스트 크기 보정
            }
        }

        // 땅 몬스터 제거
        if (groundMonsterModel != null && playerModel.isInAttackRange(groundMonsterModel.getX(), groundMonsterModel.getY())) {
            gameView.removeView(groundMonsterView);
            groundMonsterModel = null;
        }
    }

}