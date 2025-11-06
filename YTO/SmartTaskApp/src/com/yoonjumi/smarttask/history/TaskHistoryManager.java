package com.yoonjumi.smarttask.history;

import java.util.*;

public class TaskHistoryManager {
    private final Stack<TaskAction> past = new Stack<>();
    private final Stack<TaskAction> future = new Stack<>();
    private final List<HistoryListener> listeners = new ArrayList<>();
    private static final int MAX_HISTORY = 50;  // 최대 히스토리 개수
    
    public void perform(TaskAction action) {
        action.execute();
        past.push(action);
        future.clear();
        
        // 히스토리 크기 제한
        if (past.size() > MAX_HISTORY) {
            past.remove(0);
        }
        
        notifyListeners();
    }
    
    public void undo() {
        if (canUndo()) {
            TaskAction action = past.pop();
            action.revert();
            future.push(action);
            notifyListeners();
        }
    }
    
    public void redo() {
        if (canRedo()) {
            TaskAction action = future.pop();
            action.execute();
            past.push(action);
            notifyListeners();
        }
    }
    
    public boolean canUndo() { 
        return !past.isEmpty(); 
    }
    
    public boolean canRedo() { 
        return !future.isEmpty(); 
    }
    
    public String getUndoDescription() {
        return past.isEmpty() ? "" : past.peek().describe();
    }
    
    public String getRedoDescription() {
        return future.isEmpty() ? "" : future.peek().describe();
    }
    
    public int getHistorySize() {
        return past.size();
    }
    
    public void addListener(HistoryListener listener) {
        listeners.add(listener);
    }
    
    public void clear() {
        past.clear();
        future.clear();
        notifyListeners();
    }
    
    private void notifyListeners() {
        for (HistoryListener listener : listeners) {
            listener.onHistoryChanged(canUndo(), canRedo(), 
                                     getUndoDescription(), getRedoDescription());
        }
    }
}