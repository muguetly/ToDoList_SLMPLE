package com.yoonjumi.smarttask.history;

public interface TaskAction {
    void execute();
    void revert();
    String describe();
}