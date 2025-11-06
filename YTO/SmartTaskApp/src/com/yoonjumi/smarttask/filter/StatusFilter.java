package com.yoonjumi.smarttask.filter;

import com.yoonjumi.smarttask.model.*;

public class StatusFilter implements TaskFilter {
    private final TaskStatus status;
    
    public StatusFilter(TaskStatus status) {
        this.status = status;
    }
    
    @Override
    public boolean accept(Task task) {
        return status == null || task.getStatus() == status;
    }
    
    @Override
    public String describe() {
        return status == null ? "모든 상태" : "상태: " + status.toEmoji();
    }
}