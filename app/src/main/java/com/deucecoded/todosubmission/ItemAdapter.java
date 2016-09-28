package com.deucecoded.todosubmission;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private Context context;
    private List<TodoItem> itemList;

    public ItemAdapter(Context context, List<TodoItem> items) {
        this.context = context;
        this.itemList = items;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public TodoItem getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return itemList.get(i).getItemId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(context);
        textView.setText(itemList.get(i).getText());

        return textView;
    }

    public void addItem(TodoItem item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        itemList.remove(position);
        notifyDataSetChanged();
    }
}
