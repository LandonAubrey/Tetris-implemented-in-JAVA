import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 俄罗斯方块游戏的主要游戏面板类
 * 负责处理游戏逻辑、绘制游戏界面和响应用户输入
 */
public class TetrisGame extends JPanel {
    // 游戏面板的尺寸常量
    private static final int BOARD_WIDTH = 10;    // 游戏区域宽度（以方块数计）
    private static final int BOARD_HEIGHT = 20;   // 游戏区域高度（以方块数计）
    private static final int BLOCK_SIZE = 30;     // 每个方块的像素大小
    
    // 游戏核心组件
    private GameBoard gameBoard;                  // 游戏面板，管理方块的位置和移动
    private ScoreManager scoreManager;            // 分数管理器，处理分数计算和记录
    private DifficultyManager difficultyManager;  // 难度管理器，控制游戏难度
    private Timer gameTimer;                      // 游戏定时器，控制方块下落
    private boolean isGameOver;                   // 游戏结束标志
    
    // 控制按钮
    public JButton increaseDifficultyButton;     // 增加难度按钮
    public JButton decreaseDifficultyButton;     // 降低难度按钮
    public JButton restartButton;                // 重新开始按钮
    public JButton pauseButton;
    public JButton exitButton;
    public JButton leaderboardButton;
    private boolean isPaused = false;
    
    /**
     * 构造函数：初始化游戏面板和游戏组件
     */
    public TetrisGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE));
        setBackground(Color.BLACK);
        
        // 初始化游戏组件
        gameBoard = new GameBoard(BOARD_WIDTH, BOARD_HEIGHT);
        scoreManager = new ScoreManager();
        difficultyManager = new DifficultyManager();
        
        setupGame();
        setupControls();
    }
    
    /**
     * 创建一个居中放置按钮的面板
     */
    public JPanel createCenteredPanel(JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false); // 保持背景透明
        panel.add(button);
        return panel;
    }
    
    /**
     * 设置右侧控制面板
     * @param sidePanel 右侧面板
     */
    public void setupSidePanel(JPanel sidePanel) {
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Color.WHITE);
        
        // 设置按钮字体（与标签一致）
        Font buttonFont = new Font("微软雅黑", Font.BOLD, 16);
        Color buttonBg = Color.WHITE;
        Color buttonFg = new Color(50, 50, 50);
        Color buttonBorder = new Color(200, 200, 200);
        Color buttonHover = new Color(240, 240, 240);
        
        // 创建按钮
        increaseDifficultyButton = new JButton("增加难度");
        decreaseDifficultyButton = new JButton("降低难度");
        restartButton = new JButton("重新开始");
        pauseButton = new JButton("暂停");
        exitButton = new JButton("退出游戏");
        leaderboardButton = new JButton("排行榜");
        JButton[] buttons = {increaseDifficultyButton, decreaseDifficultyButton, restartButton, pauseButton, exitButton, leaderboardButton};
        
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(buttonBg);
            btn.setForeground(buttonFg);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(buttonBorder));
            btn.setContentAreaFilled(true);
            btn.setOpaque(true);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // 悬停效果
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(buttonHover);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(buttonBg);
                }
            });
        }
        
        // 设置按钮大小
        Dimension buttonSize = new Dimension(120, 36);
        increaseDifficultyButton.setPreferredSize(buttonSize);
        decreaseDifficultyButton.setPreferredSize(buttonSize);
        restartButton.setPreferredSize(buttonSize);
        pauseButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);
        leaderboardButton.setPreferredSize(buttonSize);
        
        // 按钮事件
        increaseDifficultyButton.addActionListener(e -> {
            if (!isGameOver && !isPaused) {
                difficultyManager.increaseDifficulty();
                gameTimer.setDelay(difficultyManager.getCurrentSpeed());
                repaint();
                requestFocusInWindow();
            }
        });
        decreaseDifficultyButton.addActionListener(e -> {
            if (!isGameOver && !isPaused) {
                difficultyManager.decreaseDifficulty();
                gameTimer.setDelay(difficultyManager.getCurrentSpeed());
                repaint();
                requestFocusInWindow();
            }
        });
        restartButton.addActionListener(e -> {
            restartGame();
            requestFocusInWindow();
        });
        pauseButton.addActionListener(e -> {
            if (!isGameOver) {
                if (!isPaused) {
                    gameTimer.stop();
                    isPaused = true;
                    pauseButton.setText("继续");
                } else {
                    gameTimer.start();
                    isPaused = false;
                    pauseButton.setText("暂停");
                }
                requestFocusInWindow();
            }
        });
        exitButton.addActionListener(e -> System.exit(0));
        leaderboardButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(sidePanel);
            LeaderboardManager manager = new LeaderboardManager();
            LeaderboardDialog dialog = new LeaderboardDialog(topFrame, manager);
            dialog.setVisible(true);
            // 确保对话框完全关闭后再设置焦点
            SwingUtilities.invokeLater(() -> {
                this.requestFocusInWindow();
                this.setFocusable(true);
            });
        });
    }
    
    /**
     * 重新开始游戏
     */
    private void restartGame() {
        gameBoard = new GameBoard(BOARD_WIDTH, BOARD_HEIGHT);
        scoreManager = new ScoreManager();
        difficultyManager = new DifficultyManager();
        isGameOver = false;
        gameTimer.setDelay(difficultyManager.getCurrentSpeed());
        gameTimer.restart();
        repaint();
    }
    
    /**
     * 设置游戏定时器和游戏循环
     */
    private void setupGame() {
        gameTimer = new Timer(difficultyManager.getCurrentSpeed(), e -> {
            if (!isGameOver) {
                if (!gameBoard.moveDown()) {
                    gameBoard.placeBlock();
                    if (gameBoard.isGameOver()) {
                        gameOver();
                    } else {
                        int linesCleared = gameBoard.clearLines();
                        if (linesCleared > 0) {
                            scoreManager.addScore(linesCleared);
                            difficultyManager.updateDifficulty(scoreManager.getScore());
                            gameTimer.setDelay(difficultyManager.getCurrentSpeed());
                        }
                    }
                }
                repaint();
            }
        });
        gameTimer.start();
    }
    
    /**
     * 设置键盘控制
     */
    private void setupControls() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isGameOver) return;
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:    // 左箭头：向左移动
                        gameBoard.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:   // 右箭头：向右移动
                        gameBoard.moveRight();
                        break;
                    case KeyEvent.VK_DOWN:    // 下箭头：加速下落
                        gameBoard.moveDown();
                        break;
                    case KeyEvent.VK_UP:      // 上箭头：旋转方块
                        gameBoard.rotate();
                        break;
                    case KeyEvent.VK_SPACE:   // 空格键：快速下落
                        gameBoard.dropDown();
                        break;
                }
                repaint();
            }
        });
    }
    
    /**
     * 处理游戏结束
     */
    private void gameOver() {
        isGameOver = true;
        gameTimer.stop();
        scoreManager.saveHighScore();
        // 写入排行榜
        if (scoreManager.getScore() > 1000) {
            LeaderboardManager manager = new LeaderboardManager();
            manager.saveRecord(scoreManager.getScore(), difficultyManager.getCurrentLevel());
        }
        JOptionPane.showMessageDialog(this, 
            "游戏结束！\n得分: " + scoreManager.getScore() + 
            "\n最高分: " + scoreManager.getHighScore(),
            "游戏结束",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 绘制游戏界面
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // 绘制网格线
        g2d.setColor(new Color(50, 50, 50));
        for (int i = 0; i <= BOARD_WIDTH; i++) {
            g2d.drawLine(i * BLOCK_SIZE, 0, i * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE);
        }
        for (int i = 0; i <= BOARD_HEIGHT; i++) {
            g2d.drawLine(0, i * BLOCK_SIZE, BOARD_WIDTH * BLOCK_SIZE, i * BLOCK_SIZE);
        }
        
        // 绘制游戏区域
        gameBoard.draw(g2d, BLOCK_SIZE);
    }
    
    /**
     * 获取当前分数
     */
    public int getScore() {
        return scoreManager.getScore();
    }
    
    /**
     * 获取最高分
     */
    public int getHighScore() {
        return scoreManager.getHighScore();
    }
    
    /**
     * 获取当前难度
     */
    public String getDifficulty() {
        return difficultyManager.getCurrentLevel();
    }
} 