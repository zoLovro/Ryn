package io.github.lovrohk.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Slider {
    protected float time1;
    protected float time2;
    protected int lane;
    protected float x;
    protected float y;
    protected boolean holding;
    protected boolean hit;

    public Slider(float time1, float time2, int lane) {
        this.time1 = time1;
        this.time2 = time2;
        this.lane = lane;
        this.y = 100; // start position
        this.x = 0; // start position
        this.hit = false;
        this.holding = false;
    }

    // every slider must implement its own update logic
    public abstract void update(float delta, float songTime);

    // every note must implement its own draw logic
    public abstract void draw(SpriteBatch batch, float songTime);

    // optional: generic hit detection
    public boolean isHit() {
        return hit;
    }

    public void hit() {
        hit = true;
    }

    public void holding(){ holding = true; }
    public boolean isHolding() {return holding;}

    public void startHolding() {
        holding = true;
        hit = true;
    }

    public void stopHolding() {
        holding = false;
    }

    public float getTime1() {return time1;}
    public float getTime2() {return time2;}
    public int getLane() {return lane;}
}
