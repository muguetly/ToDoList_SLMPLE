package com.yoonjumi.smarttask.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository Pattern - 데이터 저장소 역할
 * 나중에 DB 연동 시 이 클래스만 수정하면 됨
 */
public class TaskListManager {
    private final List<Task> tasks = new ArrayList<>();
    
    public void add(Task task) { tasks.add(task); }
    public void add(Task task, int position) { tasks.add(position, task); }
    public void remove(Task task) { tasks.remove(task); }
    public int indexOf(Task task) { return tasks.indexOf(task); }
    public List<Task> getAll() { return new ArrayList<>(tasks); }
    // setTasks() 삭제!
}