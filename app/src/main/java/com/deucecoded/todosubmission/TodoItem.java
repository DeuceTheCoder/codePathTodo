package com.deucecoded.todosubmission;

public class TodoItem {
    private long itemId;
    private String text;

    public TodoItem(String itemText) {
        this.text = itemText;
    }

    public TodoItem(long todoId, String itemText) {
        this.itemId = todoId;
        this.text = itemText;
    }

    public long getItemId() {
        return itemId;
    }

    public String getText() {
        return text;
    }

    public void setText(String itemText) {
        this.text = itemText;
    }
}
