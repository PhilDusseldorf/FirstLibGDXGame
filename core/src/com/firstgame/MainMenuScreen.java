package com.firstgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen{
    final Drop game;
    OrthographicCamera camera;

    private final int screenWidth = 800;
	private final int screenHeight = 480;

    public MainMenuScreen (final Drop game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, "Welcome to my first GdxGame!!! ", screenWidth / 3 , screenHeight / 2 + 50);
        game.font.draw(game.batch, "Task: Collect as many raindrops as possible", screenWidth / 3 , screenHeight / 2);
		game.font.draw(game.batch, "Tap anywhere to begin!", screenWidth / 3 , screenHeight / 2 - 50);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new MyGdxGame(game));
			dispose();
		}
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
    
}
