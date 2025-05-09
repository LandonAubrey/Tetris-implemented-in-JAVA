import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 排行榜管理类，负责保存和读取排行榜数据
 */
public class LeaderboardManager {
    private static final String FILE = "leaderboard.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static class Record {
        public int score;
        public String difficulty;
        public String time;
        public Record(int score, String difficulty, String time) {
            this.score = score;
            this.difficulty = difficulty;
            this.time = time;
        }
    }

    /**
     * 保存一条新纪录
     */
    public void saveRecord(int score, String difficulty) {
        String time = DATE_FORMAT.format(new Date());
        String line = score + "," + difficulty + "," + time + "\n";
        try {
            Files.write(Paths.get(FILE), line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("无法保存排行榜记录: " + e.getMessage());
        }
    }

    /**
     * 读取所有排行榜记录
     */
    public List<Record> loadRecords() {
        List<Record> list = new ArrayList<>();
        try {
            if (Files.exists(Paths.get(FILE))) {
                List<String> lines = Files.readAllLines(Paths.get(FILE));
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        int score = Integer.parseInt(parts[0]);
                        String difficulty = parts[1];
                        String time = parts[2];
                        list.add(new Record(score, difficulty, time));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("无法读取排行榜: " + e.getMessage());
        }
        return list;
    }
} 