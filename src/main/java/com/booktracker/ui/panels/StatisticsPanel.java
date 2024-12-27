package com.booktracker.ui.panels;

import com.booktracker.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.booktracker.service.BookService;
import com.booktracker.service.ReadingService;
import com.booktracker.exception.BookTrackerException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsPanel.class);
    private JPanel statsContainer;
    private final ReadingService readingService;
    private final BookService bookService;
    private Long currentUserId = 1L;  // 改为使用Long类型
    private final Map<String, JLabel> statLabels;
    private JComboBox<String> timeRangeCombo;

    public StatisticsPanel() {
        setLayout(new BorderLayout());
        this.readingService = new ReadingService();
        this.bookService = new BookService();
        this.statLabels = new HashMap<>();
        readingService.setCurrentUser(currentUserId);
        initializeComponents();
        refreshStatistics();
    }

    public void setCurrentUser(Long userId) {  // 修改为接收Long类型
        this.currentUserId = userId;
        readingService.setCurrentUser(userId);
        refreshStatistics();
    }

    private void initializeComponents() {
        // 创建统计数据容器
        statsContainer = new JPanel(new GridLayout(6, 1, 10, 10));
        statsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 添加统计组件
        addStatisticComponent("总阅读时间", "0小时");
        addStatisticComponent("已读页数", "0页");
        addStatisticComponent("已完成图书", "0本");
        addStatisticComponent("平均阅读速度", "0页/小时");
        addStatisticComponent("完成率", "0%");
        addStatisticComponent("平均每日阅读时长", "0小时");

        // 添加滚动支持
        JScrollPane scrollPane = new JScrollPane(statsContainer);
        add(scrollPane, BorderLayout.CENTER);

        // 添加时间范围选择
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] timeRanges = {"本日", "本周", "本月", "全部"};
        timeRangeCombo = new JComboBox<>(timeRanges);
        timeRangeCombo.addActionListener(e -> refreshStatistics());
        controlPanel.add(new JLabel("时间范围："));
        controlPanel.add(timeRangeCombo);

        // 添加刷新按钮
        JButton refreshButton = new JButton("刷新统计");
        refreshButton.addActionListener(e -> refreshStatistics());
        controlPanel.add(refreshButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void addStatisticComponent(String label, String value) {
        JPanel statPanel = new JPanel(new BorderLayout());
        statPanel.setBorder(BorderFactory.createTitledBorder(label));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font(valueLabel.getFont().getName(), Font.BOLD, 16));

        statPanel.add(valueLabel, BorderLayout.CENTER);
        statsContainer.add(statPanel);
        statLabels.put(label, valueLabel);
    }

    private void refreshStatistics() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startTime;

            switch (timeRangeCombo.getSelectedIndex()) {
                case 0: // 本日
                    startTime = now.withHour(0).withMinute(0).withSecond(0);
                    break;
                case 1: // 本周
                    startTime = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                            .withHour(0).withMinute(0).withSecond(0);
                    break;
                case 2: // 本月
                    startTime = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                    break;
                default: // 全部
                    startTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            }

            // 计算统计数据
            double totalHours = readingService.getTotalReadingHours(startTime, now);
            int totalPages = readingService.getTotalPagesRead(startTime, now);

            List<Book> books = bookService.getAllBooks();
            int completedBooks = 0;
            double totalSpeed = 0;
            int booksWithSpeed = 0;

            for (Book book : books) {
                double completion = readingService.calculateCompletionRate(book.getId());
                if (completion >= 100) {
                    completedBooks++;
                }

                double speed = readingService.calculateAverageReadingSpeed(book.getId());
                if (speed > 0) {
                    totalSpeed += speed;
                    booksWithSpeed++;
                }
            }

            double averageSpeed = booksWithSpeed > 0 ? totalSpeed / booksWithSpeed : 0;

            // 更新统计显示
            updateStatLabel("总阅读时间", String.format("%.1f小时", totalHours));
            updateStatLabel("已读页数", totalPages + "页");
            updateStatLabel("已完成图书", completedBooks + "本");
            updateStatLabel("平均阅读速度", String.format("%.1f页/小时", averageSpeed));
            updateStatLabel("完成率", String.format("%.1f%%",
                    books.isEmpty() ? 0 : (completedBooks * 100.0 / books.size())));
            updateStatLabel("平均每日阅读时长", String.format("%.1f小时",
                    totalHours / Math.max(1, java.time.Duration.between(startTime, now).toDays())));

        } catch (BookTrackerException e) {
            logger.error("获取统计数据失败", e);
            JOptionPane.showMessageDialog(this,
                    "获取统计数据失败: " + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatLabel(String label, String value) {
        JLabel valueLabel = statLabels.get(label);
        if (valueLabel != null) {
            valueLabel.setText(value);
        }
    }
}
