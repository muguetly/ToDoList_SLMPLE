package com.yoonjumi.smarttask.sort;

import com.yoonjumi.smarttask.model.Task;
import java.util.Comparator;

public interface TaskComparator extends Comparator<Task> {
    String describe();
}