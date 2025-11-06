package com.yoonjumi.smarttask.history;

import com.yoonjumi.smarttask.model.*;

public class EditTask implements TaskAction {
    private final Task task;
    private final String previousTitle;
    private final String previousDescription;
    private final Priority previousPriority;
    private final String newTitle;
    private final String newDescription;
    private final Priority newPriority;
    
    public EditTask(Task task, String newTitle, String newDescription, Priority newPriority) {
        this.task = task;
        this.previousTitle = task.getTitle();
        this.previousDescription = task.getDescription();
        this.previousPriority = task.getPriority();
        this.newTitle = newTitle;
        this.newDescription = newDescription;
        this.newPriority = newPriority;
    }
    
    @Override
    public void execute() {
        task.setTitle(newTitle);
        task.setDescription(newDescription);
        task.setPriority(newPriority);
    }
    
    @Override
    public void revert() {
        task.setTitle(previousTitle);
        task.setDescription(previousDescription);
        task.setPriority(previousPriority);
    }
    
    @Override
    public String describe() {
        return "'" + previousTitle + "' 수정";
    }
}