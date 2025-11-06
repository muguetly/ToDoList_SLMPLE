package com.yoonjumi.smarttask.model;

public enum TaskStatus {
    PENDING, COMPLETED;

    public String toEmoji() {
        switch (this) {
            case COMPLETED:
                return "✔ 완료";
            case PENDING:
            default:
                return "⌛ 진행 중";
        }
    }

    public boolean isDone() { return this == COMPLETED; }
    public boolean isPending() { return this == PENDING; }
}