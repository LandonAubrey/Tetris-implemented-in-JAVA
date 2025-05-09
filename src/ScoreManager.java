import java.io.*;
import java.nio.file.*;

/**
 * 分数管理器类
 * 负责处理游戏分数的计算、记录和保存
 */
public class ScoreManager {
    private static final String HIGH_SCORE_FILE = "highscore.txt";  // 最高分保存文件
    private int score;         // 当前游戏分数
    private int highScore;     // 历史最高分
    
    /**
     * 构造函数
     * 初始化分数并加载历史最高分
     */
    public ScoreManager() {
        this.score = 0;
        this.highScore = loadHighScore();
    }
    
    /**
     * 根据消除的行数增加分数
     * 消除的行数越多，获得的分数越高
     * @param linesCleared 消除的行数
     */
    public void addScore(int linesCleared) {
        int points = 0;
        switch (linesCleared) {
            case 1:  // 消除1行得100分
                points = 100;
                break;
            case 2:  // 消除2行得300分
                points = 300;
                break;
            case 3:  // 消除3行得500分
                points = 500;
                break;
            case 4:  // 消除4行得800分
                points = 800;
                break;
        }
        score += points;
        if (score > highScore) {
            highScore = score;
        }
    }
    
    /**
     * 保存最高分到文件
     * 如果保存失败会在控制台输出错误信息
     */
    public void saveHighScore() {
        try {
            Files.writeString(Paths.get(HIGH_SCORE_FILE), String.valueOf(highScore));
        } catch (IOException e) {
            System.err.println("无法保存最高分: " + e.getMessage());
        }
    }
    
    /**
     * 从文件加载最高分
     * 如果文件不存在或读取失败，返回0
     * @return 历史最高分
     */
    private int loadHighScore() {
        try {
            if (Files.exists(Paths.get(HIGH_SCORE_FILE))) {
                String content = Files.readString(Paths.get(HIGH_SCORE_FILE));
                return Integer.parseInt(content.trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("无法加载最高分: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * 获取当前游戏分数
     * @return 当前分数
     */
    public int getScore() {
        return score;
    }
    
    /**
     * 获取历史最高分
     * @return 最高分
     */
    public int getHighScore() {
        return highScore;
    }
} 