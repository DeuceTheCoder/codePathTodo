package com.deucecoded.todosubmission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.deucecoded.todosubmission.db.DatabaseHandler;

import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private ListView itemListView;
    private DatabaseHandler databaseHandler;
    private ItemAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler = new DatabaseHandler(this);
        setContentView(R.layout.activity_todo_list);
        itemListView = (ListView) findViewById(R.id.lvItems);
        loadListAdapter();

        itemListView.setAdapter(todoAdapter);

        setupViewClickListeners();
    }

    private void setupViewClickListeners() {
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                boolean removed = databaseHandler.deleteTodo(todoAdapter.getItem(position).getItemId());
                todoAdapter.deleteItem(position);

                return removed;
            }
        });

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                TodoItem todoItem = todoAdapter.getItem(i);
                intent.putExtra(EditItemActivity.TODO_ITEM_KEY, todoItem.getText());
                startActivityForResult(intent, i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String newValue = data.getStringExtra(EditItemActivity.TODO_ITEM_KEY);

            TodoItem item = todoAdapter.getItem(requestCode);
            item.setText(newValue);
            databaseHandler.updateTodo(item);
            todoAdapter.notifyDataSetChanged();
        }
    }

    public void onAddItem(View v) {
        EditText newItemEditText = (EditText) findViewById(R.id.etNewItem);
        String itemText = newItemEditText.getText().toString();

        long todoId = databaseHandler.insertTodo(itemText);
        TodoItem todoItem = new TodoItem(todoId, itemText);
        todoAdapter.addItem(todoItem);

        newItemEditText.setText("");
    }

    private void loadListAdapter() {
        List<TodoItem> todoItems = databaseHandler.retrieveTodos();
        todoAdapter = new ItemAdapter(this, todoItems);
    }
}
