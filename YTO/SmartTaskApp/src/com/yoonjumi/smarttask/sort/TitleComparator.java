package com.yoonjumi.smarttask.sort;

import com.yoonjumi.smarttask.model.Task;

public class TitleComparator implements TaskComparator {
    @Override
    public int compare(Task t1, Task t2) {
        return t1.getTitle().compareToIgnoreCase(t2.getTitle());
    }
    
    @Override
    public String describe() {
        return "제목 가나다순";
    }
}