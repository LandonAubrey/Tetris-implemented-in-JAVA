import java.awt.*;
import java.util.Random;

/**
 * 游戏面板类
 * 负责管理游戏区域、方块的放置和移动、消行等核心游戏逻辑
 */
public class GameBoard {
    private int[][] board;        // 游戏区域，存储已放置的方块
    private Block currentBlock;   // 当前正在下落的方块
    private int width;            // 游戏区域宽度
    private int height;           // 游戏区域高度
    private Random random;        // 随机数生成器，用于生成新方块
    
    /**
     * 构造函数
     * @param width 游戏区域宽度
     * @param height 游戏区域高度
     */
    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[height][width];
        this.random = new Random();
        spawnNewBlock();    //生成新的方块
    }
    
    /**
     * 生成新的方块
     * 随机选择一种方块类型，并将其放置在游戏区域顶部中央
     */
    public void spawnNewBlock() {
        currentBlock = new Block(random.nextInt(7));
        currentBlock.setPosition(width / 2 - 1, 0);
    }
    
    /**
     * 尝试将当前方块向左移动
     * @return 如果移动成功返回true，否则返回false
     */
    public boolean moveLeft() {
        if (canMove(currentBlock.getX() - 1, currentBlock.getY())) {
            currentBlock.moveLeft();
            return true;
        }
        return false;
    }
    
    /**
     * 尝试将当前方块向右移动
     * @return 如果移动成功返回true，否则返回false
     */
    public boolean moveRight() {
        if (canMove(currentBlock.getX() + 1, currentBlock.getY())) {
            currentBlock.moveRight();
            return true;
        }
        return false;
    }
    
    /**
     * 尝试将当前方块向下移动
     * @return 如果移动成功返回true，否则返回false
     */
    public boolean moveDown() {
        if (canMove(currentBlock.getX(), currentBlock.getY() + 1)) {
            currentBlock.moveDown();
            return true;
        }
        return false;
    }
    
    /**
     * 尝试旋转当前方块
     * 如果旋转后位置无效，则恢复原状
     */
    public void rotate() {
        currentBlock.rotate();
        if (!canMove(currentBlock.getX(), currentBlock.getY())) {
            currentBlock.rotateBack();
        }
    }
    
    /**
     * 将当前方块快速下落到底部
     */
    public void dropDown() {
        while (moveDown()) {
            // 继续下落直到不能移动
        }
    }
    
    /**
     * 检查方块是否可以移动到指定位置
     * @param newX 目标x坐标
     * @param newY 目标y坐标
     * @return 如果可以移动返回true，否则返回false
     */
    private boolean canMove(int newX, int newY) {
        int[][] shape = currentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int x = newX + j;
                    int y = newY + i;
                    if (x < 0 || x >= width || y >= height || (y >= 0 && board[y][x] != 0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    //将当前方块放置到游戏区域中
    //放置后生成新的方块
    public void placeBlock() {
        int[][] shape = currentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int x = currentBlock.getX() + j;
                    int y = currentBlock.getY() + i;
                    if (y >= 0) {
                        board[y][x] = currentBlock.getColor();
                    }
                }
            }
        }
        spawnNewBlock();
    }
    
    /**
     * 清除已填满的行
     * @return 清除的行数
     */
    public int clearLines() {
        int linesCleared = 0;
        for (int i = height - 1; i >= 0; i--) {
            boolean isLineFull = true;
            for (int j = 0; j < width; j++) {
                if (board[i][j] == 0) {
                    isLineFull = false;
                    break;
                }
            }
            if (isLineFull) {
                linesCleared++;
                // 将上面的行向下移动
                for (int k = i; k > 0; k--) {
                    System.arraycopy(board[k-1], 0, board[k], 0, width);
                }
                // 清空最上面的行
                for (int j = 0; j < width; j++) {
                    board[0][j] = 0;
                }
                i++; // 重新检查当前行，因为上面的行已经下移
            }
        }
        return linesCleared;
    }
    
    /**
     * 检查游戏是否结束
     * @return 如果游戏结束返回true，否则返回false
     */
    public boolean isGameOver() {
        return !canMove(currentBlock.getX(), currentBlock.getY());
    }
    
    /**
     * 绘制游戏区域
     * @param g2d 图形上下文
     * @param blockSize 方块大小（像素）
     */
    public void draw(Graphics2D g2d, int blockSize) {
        // 绘制已放置的方块
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j] != 0) {
                    drawBlock(g2d, j, i, board[i][j], blockSize);
                }
            }
        }
        
        // 绘制当前方块
        int[][] shape = currentBlock.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int x = currentBlock.getX() + j;
                    int y = currentBlock.getY() + i;
                    if (y >= 0) {
                        drawBlock(g2d, x, y, currentBlock.getColor(), blockSize);
                    }
                }
            }
        }
    }
    
    /**
     * 绘制单个方块
     * @param g2d 图形上下文
     * @param x 方块的x坐标
     * @param y 方块的y坐标
     * @param color 方块的颜色编号
     * @param blockSize 方块大小（像素）
     */
    private void drawBlock(Graphics2D g2d, int x, int y, int color, int blockSize) {
        Color[] colors = {
            Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW,
            Color.GREEN, Color.MAGENTA, Color.RED
        };
        
        g2d.setColor(colors[color - 1]);
        g2d.fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x * blockSize, y * blockSize, blockSize, blockSize);
    }
} 