package com.yoonjumi.smarttask.filter;

import com.yoonjumi.smarttask.model.Task;
import java.util.*;

public class CompositeFilter implements TaskFilter {
    private final List<TaskFilter> filters = new ArrayList<>();
    
    public CompositeFilter addFilter(TaskFilter filter) {
        filters.add(filter);
        return this;
    }
    
    @Override
    public boolean accept(Task task) {
        for (TaskFilter filter : filters) {
            if (!filter.accept(task)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String describe() {
        if (filters.isEmpty()) return "필터 없음";
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filters.size(); i++) {
            if (i > 0) sb.append(" + ");
            sb.append(filters.get(i).describe());
        }
        return sb.toString();
    }
    
    public void clear() {
        filters.clear();
    }
}