package com.yoonjumi.smarttask.view;

import com.yoonjumi.smarttask.controller.TaskChangeListener;
import com.yoonjumi.smarttask.model.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Observer Pattern - 관찰자 역할
 * MVC Pattern - View 역할
 */

public class TaskTableModel extends AbstractTableModel implements TaskChangeListener {
    private final String[] columns = {"제목", "설명", "우선순위", "상태"};
    private List<Task> tasks;

    public TaskTableModel(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Task task = tasks.get(row);
        return switch (col) {
            case 0 -> task.getTitle();
            case 1 -> task.getDescription();
            case 2 -> task.getPriority();
            case 3 -> task.getStatus().toEmoji();
            default -> "";
        };
    }

    public Task getTaskAt(int row) {
        return tasks.get(row);
    }

    @Override
    public void onTaskListChanged(List<Task> updatedTasks) {
        this.tasks = updatedTasks;
        fireTableDataChanged();
    }
}