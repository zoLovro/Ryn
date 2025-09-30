package io.github.lovrohk.game;

public class HealthbarManager {
    protected float health = 100.0f;
    protected float HPstat;

    public HealthbarManager(float HPstat) {
        this.HPstat = HPstat;
    }

    // getters
    public float getHealth() {
        return health;
    }

    // main functions
    public void healthDraw() { health -= 0.03f; }
    public void hit200Health() { health = Math.min(100f, health + HPstat * 2.3f); }
    public void hit50Health() { health = Math.min(100f, health + HPstat * 1.5f); }
    public void missHealth() { health -= HPstat * 3.3f; }
}
