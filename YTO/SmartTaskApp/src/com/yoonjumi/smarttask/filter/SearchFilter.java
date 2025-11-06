package com.yoonjumi.smarttask.filter;

import com.yoonjumi.smarttask.model.Task;

public class SearchFilter implements TaskFilter {
    private final String keyword;
    
    public SearchFilter(String keyword) {
        this.keyword = keyword.toLowerCase();
    }
    
    @Override
    public boolean accept(Task task) {
        if (keyword.isEmpty()) return true;
        
        return task.getTitle().toLowerCase().contains(keyword) ||
               task.getDescription().toLowerCase().contains(keyword);
    }
    
    @Override
    public String describe() {
        return "검색: " + keyword;
    }
}