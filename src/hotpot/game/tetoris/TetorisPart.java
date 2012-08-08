package hotpot.game.tetoris;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/*
 *  0  1  2  3
 *  4  5  6  7
 *  8  9 10 11
 * 12 13 14 15
 * 
 */

public class TetorisPart {

    List<Block> blocks = new ArrayList<Block>(16);
    // public boolean failing = true;//下降中フラグ
    // 中心ブロックを作成（描画はされない）
    Block centerBlock;

    public TetorisPart() {

        // ブロックの作成
        blocks.add(new Block(0, 0));
        blocks.add(new Block(0, 1));
        blocks.add(new Block(0, 2));
        blocks.add(new Block(0, 3));
        centerBlock = new Block(0, 2);

    }

    /**
     * テトリスを移動させる
     * 
     * @param afterBlocks
     */
    public void move(List<Block> afterBlocks) {
        if (blocks == null || blocks.size() != afterBlocks.size()) {
            return;
        }
        int len = blocks.size();
        for (int i = 0; i < len; i++) {
            blocks.get(i).x = afterBlocks.get(i).x;
            blocks.get(i).y = afterBlocks.get(i).y;
        }
    }

    /**
     * テトリスの落下位置を計算
     * 
     * @return
     */
    public List<Block> getFailDownPosition() {
        List<Block> result = new ArrayList<Block>();
        if (blocks == null) {
            return result;
        }
        int len = blocks.size();
        for (int i = 0; i < len; i++) {
        	Block block = new Block(blocks.get(i).x, blocks.get(i).y);
            block.y++;
            result.add(block);
        }
        return result;
    }

    // /**
    // * １段下に移動
    // *
    // * @return
    // */
    // public List<Block> faildown() {
    // for (Block block : blocks) {
    // block.y++;
    // }
    // centerBlock.y++;// 中心ブロックも移動
    // return blocks;
    // }
    //
    // /**
    // * １段上に移動
    // */
    // public void up() {
    // for (Block block : blocks) {
    // block.y--;
    // }
    // centerBlock.y--;
    // }

    // /**
    // * 一番下のポジションを返す
    // */
    // public int getBottom() {
    // int bottom = 0;
    // for (Block block : blocks) {
    // if (block.y > bottom) {
    // bottom = block.y;
    // }
    // }
    // return bottom;
    // }

    // /**
    // * 左に移動する
    // */
    // public void goLeft() {
    // Log.i("TETORIS", "LEFT");
    // List<Block> afterBlocks = new ArrayList<Block>();
    // for (Block block : blocks) {
    // int x = block.x - 1;
    // if (x < 0) {
    // afterBlocks = null;
    // return;
    // }
    // afterBlocks.add(new Block(x, block.y));
    // }
    // blocks = afterBlocks;
    // centerBlock.x--;
    // }
    //
    // /**
    // * 右に移動する
    // */
    // public void goRight() {
    // List<Block> afterBlocks = new ArrayList<Block>();
    // for (Block block : blocks) {
    // int x = block.x + 1;
    // if (x > World.WORLD_WIDTH - 1) {
    // afterBlocks = null;
    // return;
    // }
    // afterBlocks.add(new Block(x, block.y));
    // }
    // blocks = afterBlocks;
    // centerBlock.x++;
    // }
    //
    // /**
    // * 時計回りに回転する
    // */
    // public void turnLeft() {
    //
    // for (Block block : blocks) {
    // // 中心ブロックからの差分計算
    // int deltX = block.x - centerBlock.x;
    // int deltY = block.y - centerBlock.y;
    // // 差分で回転
    // block.x = -1 * deltY;
    // block.y = deltX;
    // // 差分を足して絶対座標に戻す
    // block.x += centerBlock.x;
    // block.y += centerBlock.y;
    // }
    // }
    //
    // public List<Block> getTurnLeftBlock() {
    // List<Block> result = new ArrayList<Block>();
    // for (Block block : blocks) {
    // // 中心ブロックからの差分計算
    // int deltX = block.x - centerBlock.x;
    // int deltY = block.y - centerBlock.y;
    // // 差分で回転
    // int x = -1 * deltY;
    // int y = deltX;
    // // 差分を足して絶対座標に戻す
    // x += centerBlock.x;
    // y += centerBlock.y;
    //
    // result.add(new Block(x, y));
    // }
    // return result;
    // }

}
