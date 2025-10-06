package io.github.lovrohk.game;

public class ScoreManager {
    protected int score;
    protected float accuracy;
    protected int combo;
    protected int highestCombo;


    public ScoreManager() {
        this.score = 0;
        this.accuracy = 100.00f;
        this.highestCombo = 0;
        this.combo = 0;
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

        if(highestCombo < combo) highestCombo = combo;
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
    public int getHighestCombo() {
        return highestCombo;
    }
    public int getScore() { return score; }
    public float getAccuracy() { return accuracy; }
}
