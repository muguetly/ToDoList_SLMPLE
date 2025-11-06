package com.yoonjumi.smarttask.controller;

import com.yoonjumi.smarttask.model.*;
import com.yoonjumi.smarttask.history.*;
import com.yoonjumi.smarttask.filter.*;
import com.yoonjumi.smarttask.sort.*;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskListManager repository = new TaskListManager();  // 이름 변경!
    private final TaskHistoryManager history = new TaskHistoryManager();
    private final List<TaskChangeListener> listeners = new ArrayList<>();
    
    // 필터와 정렬 설정
    private CompositeFilter currentFilter = new CompositeFilter();
    private TaskComparator currentComparator = new PriorityComparator();

    public void addListener(TaskChangeListener listener) {
        listeners.add(listener);
    }
    
    public void addHistoryListener(HistoryListener listener) {
        history.addListener(listener);
    }

    // === 기본 CRUD ===
    
    public void addTask(String title, String description, Priority priority) {
        Task task = new Task(title, description, priority);
        repository.add(task);
        notifyListeners();
    }

    public void editTask(Task task, String newTitle, String newDescription, Priority newPriority) {
        TaskAction action = new EditTask(task, newTitle, newDescription, newPriority);
        history.perform(action);
        notifyListeners();
    }

    public void completeTask(Task task) {
        TaskAction action = new CompleteTask(task);
        history.perform(action);
        notifyListeners();
    }

    public void deleteTask(Task task) {
        TaskAction action = new DeleteTask(repository, task);
        history.perform(action);
        notifyListeners();
    }

    // === Undo/Redo ===
    
    public void undo() {
        history.undo();
        notifyListeners();
    }

    public void redo() {
        history.redo();
        notifyListeners();
    }

    // === 검색 & 필터 ===
    
    public void search(String keyword) {
        currentFilter.clear();
        if (keyword != null && !keyword.trim().isEmpty()) {
            currentFilter.addFilter(new SearchFilter(keyword));
        }
        notifyListeners();
    }
    
    public void filterByPriority(Priority priority) {
        currentFilter.clear();
        if (priority != null) {
            currentFilter.addFilter(new PriorityFilter(priority));
        }
        notifyListeners();
    }
    
    public void filterByStatus(TaskStatus status) {
        currentFilter.clear();
        if (status != null) {
            currentFilter.addFilter(new StatusFilter(status));
        }
        notifyListeners();
    }
    
    public void setCompositeFilter(String keyword, Priority priority, TaskStatus status) {
        currentFilter.clear();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            currentFilter.addFilter(new SearchFilter(keyword));
        }
        if (priority != null) {
            currentFilter.addFilter(new PriorityFilter(priority));
        }
        if (status != null) {
            currentFilter.addFilter(new StatusFilter(status));
        }
        
        notifyListeners();
    }
    
    public void clearFilter() {
        currentFilter.clear();
        notifyListeners();
    }
    
    public String getCurrentFilterDescription() {
        return currentFilter.describe();
    }

    // === 정렬 ===
    
    public void sortByPriority() {
        currentComparator = new PriorityComparator();
        notifyListeners();
    }
    
    public void sortByDate(boolean ascending) {
        currentComparator = new DateComparator(ascending);
        notifyListeners();
    }
    
    public void sortByTitle() {
        currentComparator = new TitleComparator();
        notifyListeners();
    }
    
    public String getCurrentSortDescription() {
        return currentComparator.describe();
    }

    // === 조회 (필터링/정렬 적용) ===
    
    public List<Task> getTasks() {
        return repository.getAll().stream()
            .filter(currentFilter::accept)
            .sorted(currentComparator)
            .collect(Collectors.toList());
    }
    
    public List<Task> getAllTasks() {
        return repository.getAll();  // 필터 무시, 전체 반환
    }

    // === Observer 알림 ===
    
    private void notifyListeners() {
        List<Task> filteredTasks = getTasks();  // 필터링된 결과
        for (TaskChangeListener listener : listeners) {
            listener.onTaskListChanged(filteredTasks);
        }
    }
}