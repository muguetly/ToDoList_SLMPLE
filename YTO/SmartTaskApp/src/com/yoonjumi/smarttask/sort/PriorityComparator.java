package com.yoonjumi.smarttask.sort;

import com.yoonjumi.smarttask.model.*;

public class PriorityComparator implements TaskComparator {
    @Override
    public int compare(Task t1, Task t2) {
        // HIGH > MEDIUM > LOW
        return t2.getPriority().compareTo(t1.getPriority());
    }
    
    @Override
    public String describe() {
        return "우선순위 높은 순";
    }
}