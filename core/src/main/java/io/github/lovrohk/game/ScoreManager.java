package io.github.lovrohk.game;

public class ScoreManager {
    private int score;
    private float accuracy;

    public ScoreManager() {
        this.score = 0;
        this.accuracy = 100.00f;
    }

    public void update(int[] hits, int combo) {
        int miss = hits[0];
        int hit50 = hits[1];
        int hit200 = hits[2];

        // update accuracy
        accuracy = calculateAcc(hits) * 100;

        // simple scoring logic (example)
        score = hit200 * 300 + hit50 * 100 - miss * 50; // adjust as you want
        score += combo; // or combo multiplier
    }

    private float calculateAcc(int[] hits) {
        int miss = hits[0];
        int hit50 = hits[1];
        int hit200 = hits[2];

        float totalHits = hit50 + hit200 + miss;
        return accuracy = ((50 * hit50) + (200 * hit200)) / (200f * totalHits);
    }

    public int getScore() {return score;}
    public float getAccuracy() {return accuracy;}
}
