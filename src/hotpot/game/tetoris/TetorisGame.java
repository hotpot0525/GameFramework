package hotpot.game.tetoris;

import hotpot.game.framework.Game;
import hotpot.game.framework.Graphics;
import hotpot.game.framework.Input.TouchEvent;
import hotpot.game.framework.Screen;
import hotpot.game.mrnom.Assets;
import hotpot.game.mrnom.MainMenuScreen;

import java.util.List;

import android.graphics.Color;
import android.util.Log;

public class TetorisGame extends Screen {
	public World world;

	enum GameState {
		Ready, Running, Paused, GameOver
	}

	public static GameState state = GameState.Ready;

	public TetorisGame(Game game) {
		super(game);
		world = new World();
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		if (state == GameState.Ready) {
			updateReady(touchEvents);
		}
		if (state == GameState.Running) {
			updateRunning(touchEvents, deltaTime);
		}
		if (state == GameState.Paused) {
			// updatePaused(touchEvents);
		}
		if (state == GameState.GameOver) {
			updateGameOver(touchEvents);
		}

	}

	/**
	 * ゲームオーバー時の画面操作
	 * 
	 * @param touchEvents
	 */
	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x >= 128 && event.x <= 192 && event.y >= 200
						&& event.y <= 264) {
					state = GameState.Ready;
					game.setScreen(new MainMenuScreen(game));
				}
			}
		}

	}

	/**
	 * ゲーム準備中の画面操作に対する処理
	 * 
	 * @param touchEvents
	 */
	private void updateReady(List<TouchEvent> touchEvents) {
		// タッチイベントがあれば、ゲーム進行状態に移行する
		if (touchEvents.size() > 0) {
			state = GameState.Running;
		}
	}

	/**
	 * ゲーム進行中の画面操作に対する処理
	 * 
	 * @param touchEvents
	 * @param deltaTime
	 */
	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		for (TouchEvent event : touchEvents) {
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x < 64 && event.y > 416) {// 左ボタンが押されたら
					Log.i("SCREEN", "SCREEN LEFT ...");
					// world.goLeftTetoris();

				}
				if (event.x > 256 && event.y > 416) {// 右ボタンが押されたら
					// world.goRightTetoris();
				}
				if (event.x > 64 && event.x < 256 && event.y > 416) {// 左右ボタンの間をクリックしたら回転（暫定）
					// world.turnLeftTetoris();
				}
			}
		}
		world.update(deltaTime);
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.tetoris_background, 0, 0);// 背景
		g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
		g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);

		if (state == GameState.Ready) {
			drawReadyUI();
		}
		if (state == GameState.Running) {
			drawRunningUI();
		}
		if (state == GameState.Paused) {
			// drawPausedUI();
		}
		if (state == GameState.GameOver) {
			drawGameOverUI();
		}

	}

	/**
	 * ゲームオーバー時の画面描画
	 */
	private void drawGameOverUI() {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.gameOver, 62, 100);
		g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
		g.drawLine(0, 416, 480, 416, Color.BLACK);
	}

	/**
	 * ゲーム準備中の画面描画
	 */
	private void drawReadyUI() {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.ready, 47, 100);
	}

	/**
	 * ゲーム進行中の画面描画
	 */
	private void drawRunningUI() {
		drawWorld(world);

	}

	/**
	 * テトリスの描画
	 * 
	 * @param world
	 */
	private void drawWorld(World world) {
		Graphics g = game.getGraphics();

		if (world.tetoris != null) {
			for (Block block : world.tetoris.blocks) {
				g.drawRect(block.x * 32, block.y * 32, 32, 32, Color.RED);
			}
		}

		for (Block block : world.fixedBlocks) {
			g.drawRect(block.x * 32, block.y * 32, 32, 32, Color.RED);
		}

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
