package com.yoonjumi.smarttask.view;

import com.yoonjumi.smarttask.controller.TaskService;
import com.yoonjumi.smarttask.history.HistoryListener;
import com.yoonjumi.smarttask.model.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class TaskManagerFrame extends JFrame implements HistoryListener {
    private static final Color PRIMARY_COLOR = new Color(108, 92, 231);
    private static final Color SECONDARY_COLOR = new Color(255, 107, 129);
    private static final Color SUCCESS_COLOR = new Color(94, 196, 142);
    private static final Color BG_COLOR = new Color(248, 249, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(149, 165, 166);
    
    private final JTextField titleField = new JTextField(15);
    private final JTextArea descArea = new JTextArea(2, 20);
    private final JComboBox<Priority> priorityBox = new JComboBox<>(Priority.values());
    
    private final JTextField searchField = new JTextField(15);
    private final JComboBox<String> priorityFilterBox = new JComboBox<>(
        new String[]{"모든 우선순위", "HIGH", "MEDIUM", "LOW"}
    );
    private final JComboBox<String> statusFilterBox = new JComboBox<>(
        new String[]{"모든 상태", "PENDING", "COMPLETED"}
    );
    
    private final JButton addButton = new JButton("추가하기");
    private final JButton editButton = new JButton("수정");
    private final JButton doneButton = new JButton("완료");
    private final JButton deleteButton = new JButton("삭제");
    private final JButton undoButton = new JButton("실행 취소");
    private final JButton redoButton = new JButton("다시 실행");
    private final JButton searchButton = new JButton("검색");
    private final JButton clearFilterButton = new JButton("초기화");
    
    private final JButton sortPriorityButton = new JButton("우선순위↓");
    private final JButton sortDateButton = new JButton("최신순↓");
    private final JButton sortTitleButton = new JButton("제목순↓");
    
    private final JTable table = new JTable();
    private final TaskTableModel tableModel;
    private final TaskService service;
    private final TaskStatisticsPanel statsPanel;
    
    private JLabel historyStatusLabel;
    private JLabel filterStatusLabel;
    private JLabel sortStatusLabel;

    public TaskManagerFrame(TaskService service) {
        super("SmartTask Manager");
        this.service = service;
        this.tableModel = new TaskTableModel(service.getTasks());
        this.service.addListener(tableModel);
        this.service.addHistoryListener(this);
        this.statsPanel = new TaskStatisticsPanel(service);

        setupUI();
        setupTable();
        
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
    }

    private void setupUI() {
        setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("할 일 관리");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("오늘도 화이팅!");
        subtitleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BG_COLOR);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(BG_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 15, 0);
        JPanel inputCard = createInputCard();
        inputCard.setPreferredSize(new Dimension(0, 180));
        mainPanel.add(inputCard, gbc);
        
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createSearchAndFilterCard(), gbc);
        
        return mainPanel;
    }

    private JPanel createInputCard() {
        JPanel cardPanel = createCard();
        cardPanel.setLayout(new BorderLayout(15, 15));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.gridwidth = 1;
        JLabel titleLbl = createLabel("제목");
        formPanel.add(titleLbl, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 2;
        styleTextField(titleField);
        formPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        JLabel descLbl = createLabel("설명");
        formPanel.add(descLbl, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 2;
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(createRoundedBorder());
        descArea.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        formPanel.add(descScroll, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.gridwidth = 1;
        JLabel priorityLbl = createLabel("우선순위");
        formPanel.add(priorityLbl, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.3;
        styleComboBox(priorityBox);
        formPanel.add(priorityBox, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.7;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_BG);
        styleButton(addButton, PRIMARY_COLOR, true);
        addButton.addActionListener(this::onAdd);
        buttonPanel.add(addButton);
        formPanel.add(buttonPanel, gbc);
        
        cardPanel.add(formPanel, BorderLayout.CENTER);
        
        return cardPanel;
    }

    private JPanel createSearchAndFilterCard() {
        JPanel cardPanel = createCard();
        cardPanel.setLayout(new BorderLayout(0, 15));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(CARD_BG);
        
        JLabel searchLabel = new JLabel("검색:");
        searchLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        searchLabel.setForeground(TEXT_PRIMARY);
        searchLabel.setOpaque(true);
        searchLabel.setBackground(CARD_BG);
        searchPanel.add(searchLabel);
        
        styleTextField(searchField);
        searchField.setPreferredSize(new Dimension(200, 35));
        searchPanel.add(searchField);
        
        styleLinkButton(searchButton, PRIMARY_COLOR);
        searchButton.addActionListener(this::onSearch);
        searchPanel.add(searchButton);
        
        searchPanel.add(Box.createHorizontalStrut(20));
        
        JLabel priorityFilterLabel = new JLabel("우선순위:");
        priorityFilterLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        priorityFilterLabel.setForeground(TEXT_PRIMARY);
        priorityFilterLabel.setOpaque(true);
        priorityFilterLabel.setBackground(CARD_BG);
        searchPanel.add(priorityFilterLabel);
        
        styleComboBox(priorityFilterBox);
        priorityFilterBox.setPreferredSize(new Dimension(130, 35));
        priorityFilterBox.addActionListener(e -> onFilterChanged());
        searchPanel.add(priorityFilterBox);
        
        JLabel statusFilterLabel = new JLabel("상태:");
        statusFilterLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        statusFilterLabel.setForeground(TEXT_PRIMARY);
        statusFilterLabel.setOpaque(true);
        statusFilterLabel.setBackground(CARD_BG);
        searchPanel.add(statusFilterLabel);
        
        styleComboBox(statusFilterBox);
        statusFilterBox.setPreferredSize(new Dimension(120, 35));
        statusFilterBox.addActionListener(e -> onFilterChanged());
        searchPanel.add(statusFilterBox);
        
        styleLinkButton(clearFilterButton, SECONDARY_COLOR);
        clearFilterButton.addActionListener(e -> onClearFilter());
        searchPanel.add(clearFilterButton);
        
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(CARD_BG);
        
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        sortPanel.setBackground(CARD_BG);
        
        JLabel sortLabel = new JLabel("정렬:");
        sortLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        sortLabel.setForeground(TEXT_PRIMARY);
        sortLabel.setOpaque(true);
        sortLabel.setBackground(CARD_BG);
        sortPanel.add(sortLabel);
        
        styleLinkButton(sortPriorityButton, PRIMARY_COLOR);
        styleLinkButton(sortDateButton, PRIMARY_COLOR);
        styleLinkButton(sortTitleButton, PRIMARY_COLOR);
        
        sortPriorityButton.addActionListener(e -> {
            service.sortByPriority();
            updateSortStatus();
        });
        sortDateButton.addActionListener(e -> {
            service.sortByDate(false);
            updateSortStatus();
        });
        sortTitleButton.addActionListener(e -> {
            service.sortByTitle();
            updateSortStatus();
        });
        
        sortPanel.add(sortPriorityButton);
        sortPanel.add(sortDateButton);
        sortPanel.add(sortTitleButton);
        
        JPanel taskActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        taskActionPanel.setBackground(CARD_BG);
        
        styleLinkButton(editButton, new Color(255, 159, 67));
        styleLinkButton(doneButton, SUCCESS_COLOR);
        styleLinkButton(deleteButton, SECONDARY_COLOR);
        
        editButton.addActionListener(this::onEdit);
        doneButton.addActionListener(this::onDone);
        deleteButton.addActionListener(this::onDelete);
        
        taskActionPanel.add(editButton);
        taskActionPanel.add(doneButton);
        taskActionPanel.add(deleteButton);
        
        actionPanel.add(sortPanel, BorderLayout.WEST);
        actionPanel.add(taskActionPanel, BorderLayout.EAST);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_BG);
        
        cardPanel.add(searchPanel, BorderLayout.NORTH);
        cardPanel.add(scrollPane, BorderLayout.CENTER);
        cardPanel.add(actionPanel, BorderLayout.SOUTH);
        
        return cardPanel;
    }

    private void setupTable() {
        table.setModel(tableModel);
        table.setRowHeight(50);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(108, 92, 231, 30));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(CARD_BG);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(248, 249, 252));
        header.setForeground(TEXT_PRIMARY);
        header.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                Task task = tableModel.getTaskAt(row);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(CARD_BG);
                    } else {
                        c.setBackground(new Color(248, 249, 252));
                    }
                }
                
                if (task.getStatus().isDone()) {
                    c.setForeground(TEXT_SECONDARY);
                    setFont(getFont().deriveFont(Font.ITALIC));
                } else {
                    c.setForeground(TEXT_PRIMARY);
                    setFont(getFont().deriveFont(Font.PLAIN));
                }
                
                if (column == 2) {
                    Priority priority = task.getPriority();
                    switch (priority) {
                        case HIGH -> c.setForeground(SECONDARY_COLOR);
                        case MEDIUM -> c.setForeground(new Color(255, 159, 67));
                        case LOW -> c.setForeground(TEXT_SECONDARY);
                    }
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                
                return c;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onDone(null);
                }
            }
        });
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(BG_COLOR);
        
        styleLinkButton(undoButton, new Color(100, 100, 100));
        styleLinkButton(redoButton, new Color(100, 100, 100));
        
        undoButton.addActionListener(e -> service.undo());
        redoButton.addActionListener(e -> service.redo());
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
        
        historyStatusLabel = new JLabel("");
        historyStatusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        historyStatusLabel.setForeground(TEXT_SECONDARY);
        historyStatusLabel.setOpaque(true);
        historyStatusLabel.setBackground(BG_COLOR);
        
        leftPanel.add(undoButton);
        leftPanel.add(redoButton);
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(historyStatusLabel);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        centerPanel.setBackground(BG_COLOR);
        
        filterStatusLabel = new JLabel("필터: 없음");
        filterStatusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        filterStatusLabel.setForeground(TEXT_SECONDARY);
        filterStatusLabel.setOpaque(true);
        filterStatusLabel.setBackground(BG_COLOR);
        
        sortStatusLabel = new JLabel("정렬: 우선순위 높은 순");
        sortStatusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        sortStatusLabel.setForeground(TEXT_SECONDARY);
        sortStatusLabel.setOpaque(true);
        sortStatusLabel.setBackground(BG_COLOR);
        
        centerPanel.add(filterStatusLabel);
        centerPanel.add(new JLabel("|"));
        centerPanel.add(sortStatusLabel);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(BG_COLOR);
        
        JLabel tipLabel = new JLabel("Tip: 더블클릭으로 빠르게 완료 처리");
        tipLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        tipLabel.setForeground(TEXT_SECONDARY);
        tipLabel.setOpaque(true);
        tipLabel.setBackground(BG_COLOR);
        rightPanel.add(tipLabel);
        
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);
        bottomPanel.add(rightPanel, BorderLayout.EAST);
        
        return bottomPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            createShadowBorder(),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }

    private Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }

    private Border createShadowBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        );
    }

    private void styleTextField(JTextField field) {
        field.setBorder(createRoundedBorder());
        field.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        field.setBackground(new Color(250, 250, 250));
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(createRoundedBorder());
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
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    if (isPrimary) {
                        button.setBackground(color.darker());
                    } else {
                        button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
                    }
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    if (isPrimary) {
                        button.setBackground(color);
                    } else {
                        button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                    }
                }
            }
        });
    }

    private void styleLinkButton(JButton button) {
        styleLinkButton(button, PRIMARY_COLOR);
    }

    private void styleLinkButton(JButton button, Color color) {
        button.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        button.setForeground(color);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setForeground(color.darker());
                    button.setFont(new Font("맑은 고딕", Font.BOLD, 13));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setForeground(color);
                    button.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
                }
            }
        });
    }

    private void onAdd(ActionEvent e) {
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();
        Priority priority = (Priority) priorityBox.getSelectedItem();

        if (title.isEmpty()) {
            showMessage("제목을 입력해주세요", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        service.addTask(title, desc, priority);
        
        titleField.setText("");
        descArea.setText("");
        priorityBox.setSelectedIndex(0);
        
        showMessage("할 일이 추가되었습니다!", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onEdit(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Task task = tableModel.getTaskAt(row);
            
            TaskEditDialog dialog = new TaskEditDialog(this, task);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                service.editTask(task, 
                               dialog.getNewTitle(), 
                               dialog.getNewDescription(), 
                               dialog.getNewPriority());
                showMessage("수정되었습니다!", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            showMessage("수정할 작업을 선택해주세요", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onDone(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Task task = tableModel.getTaskAt(row);
            if (task.getStatus().isDone()) {
                showMessage("이미 완료된 작업입니다", JOptionPane.INFORMATION_MESSAGE);
            } else {
                service.completeTask(task);
                showMessage("완료했어요!", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            showMessage("완료할 작업을 선택해주세요", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onDelete(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "정말 삭제하시겠습니까?",
                "삭제 확인",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                Task task = tableModel.getTaskAt(row);
                service.deleteTask(task);
                showMessage("삭제되었습니다", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            showMessage("삭제할 작업을 선택해주세요", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onSearch(ActionEvent e) {
        String keyword = searchField.getText().trim();
        Priority priority = getPriorityFromFilter();
        TaskStatus status = getStatusFromFilter();
        
        service.setCompositeFilter(keyword, priority, status);
        updateFilterStatus();
    }

    private void onFilterChanged() {
        String keyword = searchField.getText().trim();
        Priority priority = getPriorityFromFilter();
        TaskStatus status = getStatusFromFilter();
        
        service.setCompositeFilter(keyword, priority, status);
        updateFilterStatus();
    }

    private void onClearFilter() {
        searchField.setText("");
        priorityFilterBox.setSelectedIndex(0);
        statusFilterBox.setSelectedIndex(0);
        service.clearFilter();
        updateFilterStatus();
    }

    private Priority getPriorityFromFilter() {
        int index = priorityFilterBox.getSelectedIndex();
        if (index == 0) return null;
        return Priority.values()[index - 1];
    }

    private TaskStatus getStatusFromFilter() {
        int index = statusFilterBox.getSelectedIndex();
        if (index == 0) return null;
        return TaskStatus.values()[index - 1];
    }

    private void updateFilterStatus() {
        filterStatusLabel.setText("필터: " + service.getCurrentFilterDescription());
    }

    private void updateSortStatus() {
        sortStatusLabel.setText("정렬: " + service.getCurrentSortDescription());
    }

    @Override
    public void onHistoryChanged(boolean canUndo, boolean canRedo, 
                                String undoDescription, String redoDescription) {
        undoButton.setEnabled(canUndo);
        redoButton.setEnabled(canRedo);
        
        StringBuilder status = new StringBuilder();
        
        if (canUndo) {
            status.append("[").append(undoDescription).append("]");
        }
        
        if (canRedo) {
            if (status.length() > 0) {
                status.append(" | ");
            }
            status.append("다시실행: ").append(redoDescription);
        }
        
        historyStatusLabel.setText(status.toString());
    }

    private void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "알림", messageType);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            TaskService service = new TaskService();
            new TaskManagerFrame(service).setVisible(true);
        });
    }
}