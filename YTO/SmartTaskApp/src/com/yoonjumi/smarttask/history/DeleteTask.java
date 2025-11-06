package com.yoonjumi.smarttask.history;

import com.yoonjumi.smarttask.model.*;

public class DeleteTask implements TaskAction {
    private final TaskListManager manager;
    private final Task task;
    private int position;
    
    public DeleteTask(TaskListManager manager, Task task) {
        this.manager = manager;
        this.task = task;
    }
    
    @Override
    public void execute() {
        position = manager.indexOf(task);
        manager.remove(task);
    }
    
    @Override
    public void revert() {
        manager.add(task, position);
    }
    
    @Override
    public String describe() {
        return "'" + task.getTitle() + "' 삭제";
    }
}