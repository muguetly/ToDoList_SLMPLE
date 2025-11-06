package com.yoonjumi.smarttask.model;

import java.time.LocalDateTime;

public class Task {
    private String title;
    private String description;
    private Priority priority;
    private TaskStatus status;
    private LocalDateTime createdAt;  // ⭐ 추가

    public Task(String title, String description, Priority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = TaskStatus.PENDING;
        this.createdAt = LocalDateTime.now();  // ⭐ 추가
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public TaskStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }  // ⭐ 추가

    // Setters - Command 패턴을 위해 필요
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }  // ⭐ 추가

    @Override
    public String toString() {
        return "[" + priority + "] " + title + " - " + description + " (" + status + ")";
    }
}