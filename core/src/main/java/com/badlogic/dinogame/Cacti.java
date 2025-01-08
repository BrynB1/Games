package com.badlogic.dinogame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Cacti {
    private Texture texture;
    private float x, y;
    private float speed;
    private float width, height;
    private Rectangle bounds;

    public Cacti(Texture texture, float x, float y, float width, float height, float speed) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.bounds = new Rectangle(x+50, y+50, width-100, height-180);
    }

    public void updatePosition(float delta) {
        x += speed * delta;
        bounds.setPosition(x+50, y+50); // Update the bounding rectangle position
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
