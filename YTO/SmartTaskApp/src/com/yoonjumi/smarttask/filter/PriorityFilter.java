package com.yoonjumi.smarttask.filter;

import com.yoonjumi.smarttask.model.*;

public class PriorityFilter implements TaskFilter {
    private final Priority priority;
    
    public PriorityFilter(Priority priority) {
        this.priority = priority;
    }
    
    @Override
    public boolean accept(Task task) {
        return priority == null || task.getPriority() == priority;
    }
    
    @Override
    public String describe() {
        return priority == null ? "모든 우선순위" : "우선순위: " + priority;
    }
}