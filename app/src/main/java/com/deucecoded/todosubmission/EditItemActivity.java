package com.deucecoded.todosubmission;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    public static final String TODO_ITEM_KEY = "TodoItem";
    private EditText itemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String item = this.getIntent().getStringExtra(TODO_ITEM_KEY);
        itemEditText = (EditText) findViewById(R.id.item_edit_text);
        itemEditText.setText(item);
    }

    public void onItemSave(View v) {
        String editedValue = itemEditText.getText().toString();
        Intent data = new Intent();
        data.putExtra(TODO_ITEM_KEY, editedValue);
        setResult(RESULT_OK, data);
        finish();
    }
}
