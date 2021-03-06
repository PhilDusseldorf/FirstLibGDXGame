package com.firstgame;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame implements Screen {
	final Drop game;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private final int screenWidth = 800;
	private final int screenHeight = 480;
	private final int iconSize = 64; // size in pixels

	private Music rainMusic;
	
	private Texture bucketImage;
	private Rectangle bucketRectangle;
	
	private Array<Rectangle> raindrops;
	private Texture dropImage;
	private Sound dropSound;
	private long lastDropTime;

	// for GUI
	private int dropCounter = 0;
	
	public MyGdxGame (final Drop game) {
		// get reference to game script
		this.game = game;

		// create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);

		// create a spriteBatch
		batch = new SpriteBatch();

		// load images into Textures, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("sprites/drop.png"));
		bucketImage = new Texture(Gdx.files.internal("sprites/bucket.png"));

		// load sounds
		dropSound = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/rain.mp3"));
		rainMusic.setLooping(true);

		
		// create a bucket
		bucketRectangle = new Rectangle();
		bucketRectangle.set((screenWidth/2 - iconSize/2), 20, iconSize, iconSize);
		
		// create raindrop array plus first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}
	
	@Override
	public void render (float delta) {
		// set background color to dark blue
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		// Update the camera
		camera.update();
		
		// render the batch
		renderBatch();
		
		// control the bucket
		playerControls();
		
		// spawn new raindrops if it is time
		checkSpawnTime();
		
		// make the drops move, remove them if they hit the bucket or exit screen
		handleDrops();
	}
	
	
	private void renderBatch() {
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(bucketImage, bucketRectangle.x, bucketRectangle.y);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		game.font.draw(game.batch, ("Counter: " + dropCounter).toString(), iconSize, screenHeight - iconSize);
		game.batch.end();
	}
	
	private void playerControls() {
		// for touch control
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucketRectangle.x = touchPos.x - iconSize / 2;
		}
		
		// for key controls
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			bucketRectangle.x -= 200 * Gdx.graphics.getDeltaTime();
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			bucketRectangle.x += 200 * Gdx.graphics.getDeltaTime();
		}
		
		// keep bucket on screen
		if (bucketRectangle.x < 0) {
			bucketRectangle.x = 0;
		}
		
		if (bucketRectangle.x > screenWidth - iconSize) {
			bucketRectangle.x = screenWidth - iconSize;
		}
	}
	
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, screenWidth - iconSize);
		raindrop.y = screenHeight;
		raindrop.width = iconSize;
		raindrop.height = iconSize;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
	
	private void checkSpawnTime() {
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
			spawnRaindrop();
		}
	}
	
	private void handleDrops() {
		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + iconSize < 0 ) {
				iter.remove();
			}
			if (raindrop.overlaps(bucketRectangle)) {
				dropSound.play();
				dropCounter++;
				iter.remove();
			}
		}
	}
	
	@Override
	public void show() {
		// start playback of background music immediatly
		rainMusic.play();
		
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
	public void dispose () {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}
}
