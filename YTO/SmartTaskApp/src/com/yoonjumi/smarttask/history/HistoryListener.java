package com.yoonjumi.smarttask.history;

public interface HistoryListener {
    void onHistoryChanged(boolean canUndo, boolean canRedo, 
                         String undoDescription, String redoDescription);
}