package com.deucecoded.todosubmission;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.deucecoded.todosubmission.db.DatabaseHandler;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private ListView itemListView;
    private List<String> items;
    private List<TodoItem> todoItems;
    private ArrayAdapter<String> itemsAdapter;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler = new DatabaseHandler(this);
        setContentView(R.layout.activity_todo_list);
        itemListView = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        itemListView.setAdapter(itemsAdapter);

        setupViewClickListeners();
    }

    private void setupViewClickListeners() {
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                intent.putExtra(EditItemActivity.TODO_ITEM_KEY, items.get(i));
                startActivityForResult(intent, i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String newValue = data.getStringExtra(EditItemActivity.TODO_ITEM_KEY);
            items.remove(requestCode);
            items.add(requestCode, newValue);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    public void onAddItem(View v) {
        EditText newItemEditText = (EditText) findViewById(R.id.etNewItem);
        String itemText = newItemEditText.getText().toString();
        databaseHandler.insertTodo(itemText);
//        TodoItem todoItem = new TodoItem(todoId, itemText);
        itemsAdapter.add(itemText);
        newItemEditText.setText("");
        writeItems();
    }

    private void readItems() {
        todoItems = databaseHandler.retrieveTodos();
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
