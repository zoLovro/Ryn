package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.lovrohk.game.ScoreManager;

public class RankAchievedManager {
    protected ScoreManager scoreManager;
    protected Texture rankX = new Texture(Gdx.files.internal("skins/testSkin/ranking-X.png"));
    protected Texture rankS = new Texture(Gdx.files.internal("skins/testSkin/ranking-S.png"));
    protected Texture rankA = new Texture(Gdx.files.internal("skins/testSkin/ranking-A.png"));
    protected Texture rankB = new Texture(Gdx.files.internal("skins/testSkin/ranking-B.png"));
    protected Texture rankC = new Texture(Gdx.files.internal("skins/testSkin/ranking-C.png"));

    public RankAchievedManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public Texture calculateAchievedRank() {
        if (scoreManager.getAccuracy() == 100f) return rankX;
        else if (scoreManager.getAccuracy() < 99.9999f && scoreManager.getAccuracy() > 95f) return rankS;
        else if (scoreManager.getAccuracy() < 95f && scoreManager.getAccuracy() > 88f) return rankA;
        else if (scoreManager.getAccuracy() < 88f && scoreManager.getAccuracy() > 80f) return rankB;
        else if (scoreManager.getAccuracy() < 80f) return rankC;
        else {
            return null;
        }
    }

    public float getRankWidth() {
        return rankX.getWidth();
    }

    public float getRankHeight() {
        return rankX.getHeight();
    }
}
