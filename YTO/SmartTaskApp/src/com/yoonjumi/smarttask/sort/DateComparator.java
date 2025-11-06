package com.yoonjumi.smarttask.sort;

import com.yoonjumi.smarttask.model.Task;

public class DateComparator implements TaskComparator {
    private final boolean ascending;
    
    public DateComparator(boolean ascending) {
        this.ascending = ascending;
    }
    
    @Override
    public int compare(Task t1, Task t2) {
        int result = t1.getCreatedAt().compareTo(t2.getCreatedAt());
        return ascending ? result : -result;
    }
    
    @Override
    public String describe() {
        return ascending ? "오래된 순" : "최신순";
    }
}