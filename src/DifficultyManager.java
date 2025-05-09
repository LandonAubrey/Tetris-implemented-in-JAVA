/**
 * 难度管理器类
 * 负责控制游戏难度，包括方块下落速度和难度等级
 */
public class DifficultyManager {
    /**
     * 不同难度等级对应的方块下落速度（毫秒）
     * 速度越快，难度越高
     */
    private static final int[] SPEEDS = {
        1000,  // 简单：1秒/格
        800,   // 中等：0.8秒/格
        500,   // 困难：0.6秒/格
        300,   // 专家：0.4秒/格
        100    // 大师：0.2秒/格
    };
    
    /**
     * 不同难度等级对应的分数阈值
     * 达到对应分数时自动提升难度
     */
    private static final int[] SCORE_THRESHOLDS = {
        0,      // 简单：初始难度
        1000,   // 中等：1000分
        3000,   // 困难：3000分
        6000,   // 专家：6000分
        10000   // 大师：10000分
    };
    
    private int currentLevel;  // 当前难度等级（0-4）
    
    /**
     * 构造函数
     * 初始化难度为简单（0级）
     */
    public DifficultyManager() {
        this.currentLevel = 0;
    }
    
    /**
     * 根据当前分数更新难度
     * @param score 当前游戏分数
     */
    public void updateDifficulty(int score) {
        for (int i = SCORE_THRESHOLDS.length - 1; i >= 0; i--) {
            if (score >= SCORE_THRESHOLDS[i]) {
                currentLevel = i;
                break;
            }
        }
    }
    
    //手动增加难度如果已经是最高难度则保持不变
    public void increaseDifficulty() {
        if (currentLevel < SPEEDS.length - 1) {
            currentLevel++;
        }
    }
    
    //手动降低难度，如果已经是最低难度则保持不变
    public void decreaseDifficulty() {
        if (currentLevel > 0) {
            currentLevel--;
        }
    }
    
    /**
     * 获取当前难度对应的方块下落速度
     * @return 下落速度（毫秒）
     */
    public int getCurrentSpeed() {
        return SPEEDS[currentLevel];
    }
    
    //获取当前难度等级的名称
    public String getCurrentLevel() {
        String[] levelNames = {"简单", "中等", "困难", "专家", "大师"};
        return levelNames[currentLevel];
    }
} 