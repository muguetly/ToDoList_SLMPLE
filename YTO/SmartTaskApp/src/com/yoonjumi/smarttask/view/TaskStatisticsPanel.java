package com.yoonjumi.smarttask.view;

import com.yoonjumi.smarttask.controller.TaskChangeListener;
import com.yoonjumi.smarttask.controller.TaskService;
import com.yoonjumi.smarttask.model.Task;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.LineBorder;
import com.yoonjumi.smarttask.model.TaskStatus;
/**
 * Observer Pattern - 관찰자 역할
 * 통계 정보를 실시간으로 업데이트
 */
public class TaskStatisticsPanel extends JPanel implements TaskChangeListener {
    private static final Color PRIMARY_COLOR = new Color(108, 92, 231);
    private static final Color SUCCESS_COLOR = new Color(94, 196, 142);
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(149, 165, 166);
    private static final Color CARD_BG = Color.WHITE;
    
    private final JLabel totalLabel = new JLabel();
    private final JLabel doneLabel = new JLabel();
    private final JLabel pendingLabel = new JLabel();
    private final JProgressBar progressBar = new JProgressBar();

    public TaskStatisticsPanel(TaskService service) {
        service.addListener(this);
        setupUI();
        updateStats(service.getTasks());
    }

    private void setupUI() {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        setBackground(new Color(248, 249, 252));
        
        // 통계 카드 생성
        add(createStatCard("📊 전체", totalLabel, PRIMARY_COLOR));
        add(createStatCard("✓ 완료", doneLabel, SUCCESS_COLOR));
        add(createStatCard("⌛ 진행중", pendingLabel, new Color(255, 159, 67)));
        
        // 진행률 바
        JPanel progressPanel = createProgressPanel();
        add(progressPanel);
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setPreferredSize(new Dimension(90, 60));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        valueLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setText("0");
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);
        
        return card;
    }

    private JPanel createProgressPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setPreferredSize(new Dimension(150, 60));
        
        JLabel label = new JLabel("진행률");
        label.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        label.setForeground(TEXT_SECONDARY);
        
        progressBar.setPreferredSize(new Dimension(100, 20));
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("맑은 고딕", Font.BOLD, 10));
        progressBar.setForeground(SUCCESS_COLOR);
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setBorderPainted(false);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(CARD_BG);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(label);
        content.add(Box.createVerticalStrut(5));
        content.add(progressBar);
        
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }

    @Override
    public void onTaskListChanged(List<Task> updatedTasks) {
        updateStats(updatedTasks);
    }

    private void updateStats(List<Task> tasks) {
        int total = tasks.size();
        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        int pending = (int) tasks.stream().filter(t -> t.getStatus() == TaskStatus.PENDING).count();
        
        totalLabel.setText(String.valueOf(total));
        doneLabel.setText(String.valueOf(done));
        pendingLabel.setText(String.valueOf(pending));
        
        if (total > 0) {
            int progress = (int) ((done * 100.0) / total);
            progressBar.setValue(progress);
            progressBar.setString(progress + "%");
        } else {
            progressBar.setValue(0);
            progressBar.setString("0%");
        }
    }
}