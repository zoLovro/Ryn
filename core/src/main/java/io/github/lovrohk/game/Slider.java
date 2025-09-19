package io.github.lovrohk.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Slider {
    private float time1;
    private float time2;
    private int lane;
    private float x;
    private float y;
    private boolean holding;
    private boolean hit;

    public Slider(float time1, float time2, int lane) {
        this.time1 = time1;
        this.time2 = time2;
        this.lane = lane;
    }

    // every slider must implement its own update logic
    public abstract void update(float delta, float songTime);

    // every note must implement its own draw logic
    public abstract void draw(SpriteBatch batch);

    // optional: generic hit detection
    public boolean isHit() {
        return hit;
    }

    public void hit() {
        hit = true;
    }

    public void holding()

    public float getTime1() {return time1;}
    public float getTime2() {return time2;}
    public int getLane() {return lane;}
}