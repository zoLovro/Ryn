package io.github.lovrohk.screensHelpful;

import com.badlogic.gdx.graphics.Texture;
import io.github.lovrohk.game.ScoreManager;

public class RankAchievedManager {
    protected ScoreManager scoreManager;
    protected Texture rankS;
    protected Texture rankA;
    protected Texture rankB;
    protected Texture rankC;

    public RankAchievedManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public Texture calculateAchievedRank() {
        if(scoreManager.getAccuracy() > 95f) return rankS;
        else if(scoreManager.getAccuracy() < 95f && scoreManager.getAccuracy() > 88f) return rankA;
        else if(scoreManager.getAccuracy() < 88f && scoreManager.getAccuracy() > 80f) return rankB;
        else if(scoreManager.getAccuracy() < 80f) return rankC;
        else{
            return null;
        }
    }
}
