//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
import javax.swing.*;
import java.awt.*;

/**
 * 俄罗斯方块游戏的主类
 * 负责创建游戏窗口和初始化游戏界面
 */
public class Main {
    public static void main(String[] args) {

        // 使用SwingUtilities.invokeLater确保在EDT线程中创建和显示GUI
        SwingUtilities.invokeLater(() -> {
            // 创建游戏主窗口
            JFrame frame = new JFrame("俄罗斯方块");        //创建游戏主窗口
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭操作
            frame.setResizable(false);                 //设置窗口不可调整大小
        
            // 创建主面板，使用BorderLayout布局管理器
            JPanel mainPanel = new JPanel(new BorderLayout());   //创建主面板
            mainPanel.setBackground(Color.WHITE);                //设置主面板背景颜色为白色
            
            // 创建游戏面板（左侧）
            TetrisGame game = new TetrisGame();                //创建游戏面板
            mainPanel.add(game, BorderLayout.CENTER);          //将游戏面板添加到主面板的中心
            
            // 创建右侧控制面板
            JPanel sidePanel = new JPanel();                  //创建右侧控制面板
            sidePanel.setPreferredSize(new Dimension(150, 0));//设置右侧控制面板的尺寸
            sidePanel.setBackground(Color.WHITE);             //设置右侧控制面板的背景颜色为白色
            mainPanel.add(sidePanel, BorderLayout.EAST);       //将右侧控制面板添加到主面板的右侧
            
            // 设置右侧面板的按钮和布局
            game.setupSidePanel(sidePanel);                   //设置右侧控制面板的按钮和布局
            
            // 创建分数显示标签
            JLabel scoreLabel = new JLabel("分数: 0");         //创建分数显示标签
            JLabel highScoreLabel = new JLabel("最高分: 0");   //创建最高分显示标签
            JLabel difficultyLabel = new JLabel("难度: 简单");  //创建难度显示标签
            
            // 设置标签字体
            Font labelFont = new Font("微软雅黑", Font.BOLD, 16);
            scoreLabel.setFont(labelFont);
            highScoreLabel.setFont(labelFont);
            difficultyLabel.setFont(labelFont);
            
            // 设置标签颜色
            scoreLabel.setForeground(Color.BLACK);
            highScoreLabel.setForeground(Color.BLACK);
            difficultyLabel.setForeground(Color.BLACK);
            
            // 将标签添加到右侧面板
            sidePanel.add(scoreLabel);
            sidePanel.add(Box.createVerticalStrut(15));
            sidePanel.add(highScoreLabel);
            sidePanel.add(Box.createVerticalStrut(15));
            sidePanel.add(difficultyLabel);
            sidePanel.add(Box.createVerticalStrut(15));
            // 添加操作说明
            JLabel helpLabel = new JLabel(
                "<html><body>"
                + "操作说明：<br>"
                + "↑：旋转<br>"
                + "↓：加速下落<br>"
                + "←→：左右移动<br>"
                + "空格：快速下落"
                + "</body></html>"
            );
            Font helpFont = new Font("微软雅黑", Font.PLAIN, 12);
            helpLabel.setFont(helpFont);
            helpLabel.setForeground(Color.BLACK);
            helpLabel.setHorizontalAlignment(SwingConstants.LEFT);
            sidePanel.add(helpLabel);
            sidePanel.add(Box.createVerticalStrut(15));
            sidePanel.add(game.createCenteredPanel(game.increaseDifficultyButton));
            sidePanel.add(Box.createVerticalStrut(5));
            sidePanel.add(game.createCenteredPanel(game.decreaseDifficultyButton));
            sidePanel.add(Box.createVerticalStrut(5));
            sidePanel.add(game.createCenteredPanel(game.restartButton));
            sidePanel.add(Box.createVerticalStrut(5));
            sidePanel.add(game.createCenteredPanel(game.pauseButton));
            sidePanel.add(Box.createVerticalStrut(5));
            sidePanel.add(game.createCenteredPanel(game.exitButton));
            sidePanel.add(Box.createVerticalStrut(5));
            sidePanel.add(game.createCenteredPanel(game.leaderboardButton));
            
            // 创建定时器，每100毫秒更新一次分数显示
            Timer updateTimer = new Timer(100, e -> {
                scoreLabel.setText("分数: " + game.getScore());
                highScoreLabel.setText("最高分: " + game.getHighScore());
                difficultyLabel.setText("难度: " + game.getDifficulty());
            });
            updateTimer.start();
            
            // 将主面板添加到窗口
            frame.add(mainPanel);
            // 调整窗口大小以适应内容
            frame.pack();
            // 将窗口居中显示
            frame.setLocationRelativeTo(null);
            // 显示窗口
            frame.setVisible(true);
        });
    }
}