package io.github.lovrohk.game;

public class HealthbarManager {
    protected float health = 100.0f;
    protected float HP;

    public HealthbarManager(float HP) {
        this.HP = HP;
    }

    // getters
    public float getHealth() {
        return health;
    }

    public void missHealth() {
        health -= HP * 2.4f;
    }
    
}
