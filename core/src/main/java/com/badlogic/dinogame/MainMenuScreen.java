package com.badlogic.dinogame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MainMenuScreen implements Screen {
    private final dinogame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Rectangle startButtonBounds;
    private Rectangle controlButtonBounds;
    private GlyphLayout titleLayout;
    private GlyphLayout startButtonLayout;
    private GlyphLayout controlButtonLayout;


    public MainMenuScreen(final dinogame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(2); // Increase font size for better visibility
        this.shapeRenderer = new ShapeRenderer();


        // Initialize layout objects for centering the text
        this.titleLayout = new GlyphLayout();
        this.startButtonLayout = new GlyphLayout();
        this.controlButtonLayout = new GlyphLayout();


        // Define button bounds (position and size)
        startButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - 200) / 2,
            (Gdx.graphics.getHeight() - 60) / 2,
            200,
            60
        );
        controlButtonBounds = new Rectangle(
            (Gdx.graphics.getWidth() - 200) / 2,
            (Gdx.graphics.getHeight() / 2) - 100,
            200,
            60
        );
    }


    @Override
    public void show() {
        // Additional initialization code can go here
    }


    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1); // Background color


        // Enable blending for transparency (useful for buttons)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        // Draw the button background shapes first
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.5f, 1.0f, 1); // Light blue for buttons
        shapeRenderer.rect(startButtonBounds.x, startButtonBounds.y,
            startButtonBounds.width, startButtonBounds.height);
        shapeRenderer.end();


        // Then draw text (title and button labels)
        batch.begin();


        // Update layout with the text to be drawn
        titleLayout.setText(font, "Dino Game");
        startButtonLayout.setText(font, "START");



        // Draw title centered on the screen
        font.setColor(1, 1, 1, 1); // White color for the title
        font.draw(batch, "Dino Game",
            (Gdx.graphics.getWidth() - titleLayout.width) / 2,
            Gdx.graphics.getHeight() / 2 + 100); // Position slightly above center


        // Draw button text (centered within their respective bounds)
        font.draw(batch, "START",
            startButtonBounds.x + (startButtonBounds.width - startButtonLayout.width) / 2,
            startButtonBounds.y + (startButtonBounds.height + startButtonLayout.height) / 2);



        batch.end();


        // Handle user input (button clicks)
        handleInput();
    }


    private void handleInput() {
        if (Gdx.input.isTouched()) {
            // Get touch coordinates
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Adjust Y for screen coordinates


            // Check if start button is touched
            if (startButtonBounds.contains(touchX, touchY)) {
                game.setScreen(new GameScreen(game)); // Switch to the GameScreen
                dispose(); // Clean up resources for this screen
            }
        }
    }


    @Override
    public void resize(int width, int height) {
        // Handle resizing if needed
    }


    @Override
    public void pause() {
        // Handle pause if needed
    }


    @Override
    public void resume() {
        // Handle resume if needed
    }


    @Override
    public void hide() {
        // Handle hiding the screen if needed
    }


    @Override
    public void dispose() {
        // Clean up resources
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}

