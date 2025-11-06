package com.yoonjumi.smarttask.filter;

import com.yoonjumi.smarttask.model.Task;

public interface TaskFilter {
    boolean accept(Task task);
    String describe();
}