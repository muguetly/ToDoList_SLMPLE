package com.yoonjumi.smarttask.history;

import com.yoonjumi.smarttask.model.*;

public class CompleteTask implements TaskAction {
    private final Task task;
    private final TaskStatus previousStatus;
    
    public CompleteTask(Task task) {
        this.task = task;
        this.previousStatus = task.getStatus();
    }
    
    @Override
    public void execute() {
        task.setStatus(TaskStatus.COMPLETED);
    }
    
    @Override
    public void revert() {
        task.setStatus(previousStatus);
    }
    
    @Override
    public String describe() {
        return "'" + task.getTitle() + "' 완료";
    }
}