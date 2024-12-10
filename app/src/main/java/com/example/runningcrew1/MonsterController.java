package com.example.runningcrew1;

import android.content.Context;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 김세훈 (20201788)

public class MonsterController {
    private Context context;
    private FrameLayout gameView;

    private Random random = new Random();

    private List<MonsterModel> monsterModels = new ArrayList<>();
    private List<MonsterView> monsterViews = new ArrayList<>();

    private int screenWidth, screenHeight;


    public MonsterController(Context context, FrameLayout gameView, int screenWidth, int screenHeight) {
        this.context = context;
        this.gameView = gameView;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void spawnMonster() {
        float spawnX = screenWidth;
        float spawnY = random.nextInt(screenHeight / 2) + screenHeight / 4f;
        MonsterModel monsterModel = new MonsterModel(spawnX, spawnY, 1, 1);
        MonsterView monsterView = new MonsterView(context, monsterModel);

        monsterModels.add(monsterModel);
        monsterViews.add(monsterView);

        gameView.addView(monsterView);
    }

    public void updateMonsters() {
        for (int i = monsterModels.size() - 1; i >= 0; i--) {
            MonsterModel monster = monsterModels.get(i);
            monster.updatePosition();

            if (monster.getX() + 100 < 0) {
                gameView.removeView(monsterViews.get(i));
                monsterModels.remove(i);
                monsterViews.remove(i);
            }
        }
    }

    public void handleMonsterCollisions(PlayerModel playerModel) {
        for (int i = monsterModels.size() - 1; i >= 0; i--) {
            MonsterModel monster = monsterModels.get(i);
            if (playerModel.checkCollision(monster.getX(), monster.getY())) {
                playerModel.setAlive(false);
                break; // 충돌 후 게임 종료
            }
        }
    }

    public void attackMonsters(PlayerModel playerModel) {
        for (int i = monsterModels.size() - 1; i >= 0; i--) {
            MonsterModel monster = monsterModels.get(i);

            if (monster.isActive() && playerModel.isInAttackRange(monster.getX(), monster.getY())) {
                monster.deactivate();
                int finalI = i;

                gameView.removeView(monsterViews.get(finalI));

                monsterModels.remove(i);
                monsterViews.remove(i);
            }
        }
    }

    public void invalidateMonsterViews() {
        for (MonsterView monsterView : monsterViews) {
            monsterView.invalidate();
        }
    }
}
