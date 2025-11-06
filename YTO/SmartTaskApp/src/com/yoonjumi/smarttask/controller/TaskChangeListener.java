package com.yoonjumi.smarttask.controller;

import com.yoonjumi.smarttask.model.Task;
import java.util.List;

public interface TaskChangeListener {
    void onTaskListChanged(List<Task> tasks);
}