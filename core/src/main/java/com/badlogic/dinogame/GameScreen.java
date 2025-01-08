package com.badlogic.dinogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {
    private final dinogame game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer; // Used for drawing debug rectangles
    private BitmapFont font;
    private BitmapFont gameOverFont;
    private float score = 0; // Timer/Score variable

    // Textures
    private Texture cityBackground1;
    private Texture dinoRightTexture;
    private Texture[] cactiTextures;

    // Sprite and position data
    private Rectangle dinobounds;
    private float dinoX, dinoY;
    private float dinoVelocityY = 0;
    private float gravity = -200f;
    private float jumpVelocity = 300f;
    private boolean isGameOver;
    private boolean isGameStarted;

    private List<Cacti> cacti;
    private int lives;
    private Random random;

    // Scrolling background variables
    private float bgScrollSpeed = 4.0f;
    private float bgOffset = 0;

    // Cacti spawn variables
    private float cactiSpawnTimer = 0f;
    private float cactiSpawnDelay = 3f; // Spawn a new cactus every 3 seconds

    public GameScreen(final dinogame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer(); // Initialize ShapeRenderer
        this.font = new BitmapFont(); // Default font
        this.gameOverFont = new BitmapFont(); // Game over font
        gameOverFont.getData().setScale(2.0f); // Scale for visibility

        // Load assets
        cityBackground1 = new Texture("dinobackground.png");
        dinoRightTexture = new Texture("pixeldino.png");

        // Initialize cacti textures
        cactiTextures = new Texture[]{
            new Texture("cactus1.png"),
            new Texture("cactus2.png"),
            new Texture("cactus3.png")
        };

        resetGame();
    }

    private void resetGame() {
        dinoX = 50;
        dinoY = 125;
        dinobounds = new Rectangle(dinoX, dinoY, dinoRightTexture.getWidth(), dinoRightTexture.getHeight());
        dinoVelocityY = 0;
        isGameOver = false;
        isGameStarted = false;
        lives = 3;

        random = new Random();
        cacti = new ArrayList<>();
        spawnCacti(1); // Start with one cactus
        cactiSpawnTimer = 0f; // Reset cactus timer
    }

    private void spawnCacti(int count) {
        for (int i = 0; i < count; i++) {
            float cactiX = Gdx.graphics.getWidth();
            float cactiY = 85;
            Texture texture = cactiTextures[MathUtils.random(cactiTextures.length - 1)];
            Cacti cacti = new Cacti(texture, cactiX, cactiY, 200, 300, -200);
            this.cacti.add(cacti);
        }
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        if (!isGameStarted) {
            if (Gdx.input.isTouched()) {
                isGameStarted = true;
            }
        } else {
            if (!isGameOver) {
                // Update score
                score += delta;

                // Scroll background
                bgOffset += bgScrollSpeed * delta;
                if (bgOffset > cityBackground1.getWidth()) {
                    bgOffset -= cityBackground1.getWidth();
                }

                // Draw background
                batch.draw(cityBackground1, -bgOffset, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                batch.draw(cityBackground1, -bgOffset + cityBackground1.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                // Draw dino
                batch.draw(dinoRightTexture, dinoX, dinoY, dinoRightTexture.getWidth(), dinoRightTexture.getHeight());

                // Draw score
                drawScore();

                // Update and draw cacti
                for (Cacti cacti : cacti) {
                    cacti.updatePosition(delta);
                    cacti.render(batch);
                }

                // Spawn cacti
                cactiSpawnTimer += delta;
                if (cactiSpawnTimer >= cactiSpawnDelay) {
                    cactiSpawnTimer = 0f;
                    spawnCacti(1);
                }

                applyGravity(delta);
                handleInput();
                checkCollisions();
            } else {
                drawGameOver();
                if (Gdx.input.isTouched()) {
                    resetGame();
                }
            }
        }
        batch.end();
    }

    private void drawScore() {
        font.setColor(0, 0, 0, 1); //Black
        String scoreText = String.format("Score: %d", (int) score); // Integer score
        font.draw(batch, scoreText, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 10);
        String livesText = String.format("Lives: %d", lives);
        font.draw(batch, livesText, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 30);
    }


    private void drawGameOver() {
        gameOverFont.draw(batch, "Game Over!", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 + 50);
        gameOverFont.draw(batch, "Tap to Respawn", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
    }

    private void applyGravity(float delta) {
        dinoVelocityY += gravity * delta;
        dinoY += dinoVelocityY * delta;
        if (dinoY < 125) {
            dinoY = 125;
            dinoVelocityY = 0;
        } else if (dinoY + dinoRightTexture.getHeight() > Gdx.graphics.getHeight()) {
            dinoY = Gdx.graphics.getHeight() - dinoRightTexture.getHeight();
            dinoVelocityY = 0;
        }
        dinobounds.setPosition(dinoX, dinoY);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            dinoVelocityY = jumpVelocity;
        }
    }

    private void checkCollisions() {
        for (int i = cacti.size() - 1; i >= 0; i--) {
            Cacti cacti = this.cacti.get(i);
            if (dinobounds.overlaps(cacti.getBounds())) {
                this.cacti.remove(i); // Remove the cactus
                lives--;
                if (lives == 0) {
                    isGameOver = true;
                }
                break;
            }
        }
    }
    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        gameOverFont.dispose();
        cityBackground1.dispose();
        dinoRightTexture.dispose();
        for (Texture texture : cactiTextures) {
            texture.dispose();
        }
    }
}
