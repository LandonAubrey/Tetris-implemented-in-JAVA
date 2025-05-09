import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 排行榜弹窗类，支持按难度筛选并显示所有超过1000分的记录
 */
public class LeaderboardDialog extends JDialog {
    private JTable table;
    private JComboBox<String> difficultyBox;
    private LeaderboardManager manager;
    private List<LeaderboardManager.Record> allRecords;

    public LeaderboardDialog(JFrame parent, LeaderboardManager manager) {
        super(parent, "排行榜", true);
        this.manager = manager;
        this.allRecords = manager.loadRecords();
        setSize(420, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // 难度选择下拉框
        String[] difficulties = {"全部", "简单", "中等", "困难", "专家", "大师"};
        difficultyBox = new JComboBox<>(difficulties);
        difficultyBox.addActionListener(e -> refreshTable());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("筛选难度："));
        topPanel.add(difficultyBox);
        add(topPanel, BorderLayout.NORTH);

        // 表格
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        refreshTable();
        
        // 禁用默认按钮，防止空格键触发
        getRootPane().setDefaultButton(null);
        
        // 添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 确保窗口关闭时释放所有资源
                dispose();
            }
        });
        
        // 设置模态对话框的默认关闭操作
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void refreshTable() {
        String selected = (String) difficultyBox.getSelectedItem();
        List<LeaderboardManager.Record> filtered = allRecords.stream()
                .filter(r -> r.score > 1000)
                .filter(r -> selected.equals("全部") || r.difficulty.equals(selected))
                .sorted((a, b) -> b.score - a.score)
                .collect(Collectors.toList());
        String[] columns = {"分数", "难度", "时间"};
        Object[][] data = new Object[filtered.size()][3];
        for (int i = 0; i < filtered.size(); i++) {
            data[i][0] = filtered.get(i).score;
            data[i][1] = filtered.get(i).difficulty;
            data[i][2] = filtered.get(i).time;
        }
        table.setModel(new DefaultTableModel(data, columns));
    }
} 