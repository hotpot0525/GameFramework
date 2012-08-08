package hotpot.game.tetoris;

import hotpot.game.tetoris.TetorisGame.GameState;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * 
 * worldで管理するもの
 * 
 * ・落下中のテトリス - 入力に対して、移動可能かチェックする - 定期的に落下させる - テトリスが固定化したらフィールド値を更新する -
 * テトリスはブロックだけ取り出し、リストで保持
 * 
 * - １列埋まったらフィールド値を書き換える - フィールド値をもとにブロックを移動させる
 * 
 * 描画クラスでやること - worldクラスから落下中テトリスとブロックのリストを入手 - 落下中テトリスとブロックのリストを元に描画
 * 
 * 
 * @author 115080A004H3K
 * 
 */
public class World {
	public static final int WORLD_WIDTH = 10;
	public static final int WORLD_HEIGHT = 12;
	public static boolean field[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];// 何もなければfalse

	public static final float UPDATE_INTERVAL = 0.5f;

	public float tick = UPDATE_INTERVAL;
	public float tickTime = 0;

	// /**
	// * 新しいテトリスを発生させるかどうか判定するフラグ
	// */
	// public boolean newTetoris = false;

	// 落下中のテトリス
	TetorisPart tetoris;
	// 固定化したテトリス
	List<Block> fixedBlocks;

	/**
	 * コンストラクタ
	 */
	public World() {
		tetoris = new TetorisPart();
		fixedBlocks = new ArrayList<Block>();
		// フィールドの初期化
		for (int i = 0; i < WORLD_WIDTH; i++) {
			for (int j = 0; j < WORLD_HEIGHT; j++) {
				field[i][j] = false;// 何もないのでfalse
			}
		}
	}

	/**
	 * テトリスの世界を進行させる
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		// テトリス生成フラグがたっていれば、新しく生成する
		if (tetoris == null) {
			tetoris = new TetorisPart();
		}
		tickTime += deltaTime;
		while (tickTime > tick) {
			tickTime -= tick;

			List<Block> expectBlocks = tetoris.getFailDownPosition();
			if (checkFailDown(expectBlocks)) {
				// テトリスが落下可能なら落下させる
				tetoris.move(expectBlocks);
			} else {
				// 落下できなければ固定ブロックにする
				addBlockList(tetoris);
				tetoris = null;
			}

			// ライン削除処理
			List<Integer> deleteLine = ckeckDeleteLine();
			if (deleteLine.size() > 0) {
				deleteLines(deleteLine);
			}

			// ゲームオーバー判定
			checkGameOver();
		}
	}

	/**
	 * ゲームオーバーを判定する
	 */
	private void checkGameOver() {
		
		for (Block block : fixedBlocks) {
			if (block.y < 0) {
				TetorisGame.state = GameState.GameOver;
			}
		}

	}

	/**
	 * ラインを削除できるかチェックする
	 * 
	 * @return
	 */
	private List<Integer> ckeckDeleteLine() {
		List<Integer> deleteLine = new ArrayList<Integer>();
		for (int h = 0; h < WORLD_HEIGHT; h++) {
			for (int w = 0; w < WORLD_WIDTH; w++) {
				if (field[w][h] == false) {
					break;
				}
				if (w == (WORLD_WIDTH - 1)) {
					deleteLine.add(h);
				}
			}
		}
		return deleteLine;
	}

	/**
	 * 指定された行を削除する
	 * 
	 * @param deleteLine
	 *            削除対象ライン番号のリスト
	 */
	private void deleteLines(List<Integer> deleteLine) {
		// 削除する
		for (int i : deleteLine) {
			for (int j = 0; j < WORLD_HEIGHT; j++) {
				field[i][j] = false;
			}
		}

		// ブロックを落下させる
		while (deleteLine.size() > 0) {
			int line = getFirstLine(deleteLine);
			deleteLine.remove(line);
			failDownBlock(line);
		}
	}

	/**
	 * 指定された行より上のブロックを落下させる
	 * 
	 * @param line
	 */
	private void failDownBlock(int line) {
		for (int height = line - 1; height < 0; height--) {
			for (int i = 0; i < WORLD_WIDTH; i++) {
				if (height == 0) {
					field[i][height] = false;
				} else {
					field[i][height] = field[i][height - 1];
				}
			}
		}

	}

	// 削除したラインの一番上の段を取得
	private int getFirstLine(List<Integer> deleteLine) {
		int result = 0;
		for (int num : deleteLine) {
			if (result > num) {
				result = num;
			}
		}
		return result;
	}

	/**
	 * テトリスを固定ブロックとして追加する
	 * 
	 * @param tetoris2
	 */
	private void addBlockList(TetorisPart tetoris2) {
		for (Block block : tetoris2.blocks) {
			fixedBlocks.add(block);
		}
	}

	/**
	 * 落下できるかチェック
	 * 
	 * @param expectBlocks
	 * 
	 * @return trueなら落下可能。falseなら落下不可
	 */
	private boolean checkFailDown(List<Block> expectBlocks) {
		boolean DOWN_OK = true;
		boolean DOWN_NG = false;
		
		List<Block> blocks = expectBlocks;

		for (Block block : blocks) {
			if (block.x < 0 || block.x > WORLD_WIDTH - 1 || block.y < 0
					|| block.y > WORLD_HEIGHT - 1) {
				Log.i("CheckFailDown", "x:"+block.x+", y:"+block.y);
				return DOWN_NG;
			}
			if (field[block.x][block.y] == true) {
				Log.i("CheckFailDown", "x:"+block.x+", y:"+block.y);
				return DOWN_NG;
			}
		}
		return DOWN_OK;
	}

	// /**
	// * 回転可能かチェックし、可能ならテトリスを回転させる
	// */
	// public void turnLeftTetoris() {
	// TetorisPart tetoris = tetorisList.get(tetorisList.size() - 1);
	// List<Block> afterBlocks = tetoris.getTurnLeftBlock(); // 回転後の予定座標を取得
	// // 回転可能だったかチェック
	// for (Block block : afterBlocks) {
	// if (block.x < 0 || block.x > WORLD_WIDTH - 1) {// 左右にはみ出るようなら回転しない
	// return;
	// }
	// if (block.y < 0 || block.y > WORLD_HEIGHT - 1) {// 空や地面を超えるよなら回転しない
	// return;
	// }
	//
	// if (field[block.x][block.y] == true) {// 回転後に障害物があれば回転しない
	// return;
	// }
	// }
	// tetoris.turnLeft();
	// }

	// /**
	// * テトリスを右に移動させる
	// */
	// public void goRightTetoris() {
	//
	// TetorisPart tetoris = tetorisList.get(tetorisList.size() - 1);
	//
	// // 移動先が移動可能かチェック
	// for (Block block : tetoris.blocks) {
	// if (block.x + 1 > WORLD_WIDTH - 1) {
	// return;
	// }
	// if (field[block.x + 1][block.y] == true) {
	// return;
	// }
	// }
	// tetoris.goRight();
	// }
}
