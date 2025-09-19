package io.github.lovrohk.game;

public class ScoreManager {
    private int score;
    private float accuracy;
    private int combo;

    public ScoreManager() {
        this.score = 0;
        this.accuracy = 100.00f;
    }

    public void update(int[] hits) {
        int miss = hits[0];
        int hit50 = hits[1];
        int hit200 = hits[2];

        // update accuracy
        accuracy = calculateAcc(hits) * 100;

        // scoring
        score = hit200 * 300 + hit50 * 100 - miss * 50;
        score += combo * 10; // example: combo bonus
    }

    private float calculateAcc(int[] hits) {
        int miss = hits[0];
        int hit50 = hits[1];
        int hit200 = hits[2];

        float totalHits = hit50 + hit200 + miss;
        return accuracy = ((50 * hit50) + (200 * hit200)) / (200f * totalHits);
    }

    // combo methods
    public void addCombo() {
        combo++;
    }

    public void resetCombo() {
        combo = 0;
    }

    // getters
    public int getCombo() { return combo; }
    public int getScore() { return score; }
    public float getAccuracy() { return accuracy; }
}
