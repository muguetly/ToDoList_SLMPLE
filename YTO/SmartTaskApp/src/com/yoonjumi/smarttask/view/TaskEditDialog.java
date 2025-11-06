package com.yoonjumi.smarttask.view;

import com.yoonjumi.smarttask.model.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TaskEditDialog extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(108, 92, 231);
    private static final Color BG_COLOR = new Color(248, 249, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    
    private final JTextField titleField;
    private final JTextArea descArea;
    private final JComboBox<Priority> priorityBox;
    private final JButton saveButton = new JButton("💾 저장");
    private final JButton cancelButton = new JButton("✕ 취소");
    
    private boolean confirmed = false;

    public TaskEditDialog(Frame parent, Task task) {
        super(parent, "할 일 수정", true);
        
        titleField = new JTextField(task.getTitle(), 20);
        descArea = new JTextArea(task.getDescription(), 3, 20);
        priorityBox = new JComboBox<>(Priority.values());
        priorityBox.setSelectedItem(task.getPriority());
        
        setupUI();
        setupListeners();
        
        setSize(500, 350);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(BG_COLOR);
    }

    private void setupUI() {
        setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("✏️ 할 일 수정하기");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BG_COLOR);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTitle = new JLabel("제목");
        lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        styleTextField(titleField);
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        formPanel.add(lblTitle);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(titleField);
        formPanel.add(Box.createVerticalStrut(15));
        
        JLabel lblDesc = new JLabel("설명");
        lblDesc.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lblDesc.setForeground(TEXT_PRIMARY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        descArea.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        formPanel.add(lblDesc);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(descScroll);
        formPanel.add(Box.createVerticalStrut(15));
        
        JLabel lblPriority = new JLabel("우선순위");
        lblPriority.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        lblPriority.setForeground(TEXT_PRIMARY);
        lblPriority.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        priorityBox.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        priorityBox.setBackground(Color.WHITE);
        priorityBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        priorityBox.setMaximumSize(new Dimension(200, 35));
        
        formPanel.add(lblPriority);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(priorityBox);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BG_COLOR);
        
        styleButton(cancelButton, new Color(150, 150, 150), false);
        styleButton(saveButton, PRIMARY_COLOR, true);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        saveButton.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "제목을 입력해주세요 🌱", 
                    "알림", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            confirmed = true;
            dispose();
        });
        
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
    }

    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        field.setBackground(new Color(250, 250, 250));
    }

    private void styleButton(JButton button, Color color, boolean isPrimary) {
        button.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (isPrimary) {
            button.setBackground(color);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
            button.setForeground(color);
        }
        
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 1, true),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getNewTitle() {
        return titleField.getText().trim();
    }

    public String getNewDescription() {
        return descArea.getText().trim();
    }

    public Priority getNewPriority() {
        return (Priority) priorityBox.getSelectedItem();
    }
}