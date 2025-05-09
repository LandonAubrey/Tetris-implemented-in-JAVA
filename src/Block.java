/**
 * 俄罗斯方块类
 * 负责管理单个方块的形状、位置和旋转
 */
public class Block {
    /**
     * 所有可能的方块形状
     * 每个形状用二维数组表示，1-7表示不同的颜色
     * 0表示空白位置
     */
    private static final int[][][] SHAPES = {
        // I形方块
        {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        },
        // J形方块
        {
            {2, 0, 0},
            {2, 2, 2},
            {0, 0, 0}
        },
        // L形方块
        {
            {0, 0, 3},
            {3, 3, 3},
            {0, 0, 0}
        },
        // O形方块
        {
            {4, 4},
            {4, 4}
        },
        // S形方块
        {
            {0, 5, 5},
            {5, 5, 0},
            {0, 0, 0}
        },
        // T形方块
        {
            {0, 6, 0},
            {6, 6, 6},
            {0, 0, 0}
        },
        // Z形方块
        {
            {7, 7, 0},
            {0, 7, 7},
            {0, 0, 0}
        }
    };
    
    private int[][] shape;    // 当前方块的形状
    private int x;            // 方块在游戏区域中的x坐标
    private int y;            // 方块在游戏区域中的y坐标
    private int color;        // 方块的颜色编号（1-7）
    
    /**
     * 构造函数
     * @param type 方块类型（0-6，对应7种不同的方块）
     */
    public Block(int type) {
        this.shape = SHAPES[type];
        this.color = type + 1;
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * 设置方块在游戏区域中的位置
     * @param x x坐标
     * @param y y坐标
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    //向左移动方块
    public void moveLeft() {
        x--;
    }
    
    //向右移动方块
    public void moveRight() {
        x++;
    }
    
    //向下移动方块
    public void moveDown() {
        y++;
    }
    
    
    //顺时针旋转方块
    public void rotate() {
        int[][] newShape = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                newShape[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        shape = newShape;
    }
    
    //逆时针旋转方块（用于旋转失败时恢复）
    public void rotateBack() {
        int[][] newShape = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                newShape[shape[i].length - 1 - j][i] = shape[i][j];
            }
        }
        shape = newShape;
    }
    
    //获取当前方块的形状
    public int[][] getShape() {
        return shape;
    }
    
    //获取方块的x坐标
    public int getX() {
        return x;
    }
    
    //获取方块的y坐标   
    public int getY() {
        return y;
    }
    
    /**
     * 获取方块的颜色编号
     * @return 颜色编号（1-7）
     */
    public int getColor() {
        return color;
    }
} 