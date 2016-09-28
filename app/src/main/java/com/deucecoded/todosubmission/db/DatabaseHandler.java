package com.deucecoded.todosubmission.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.deucecoded.todosubmission.TodoItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();
    public static String DB_NAME = "TodoDB";
    private static int DB_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoTable.CREATE_TABLE);
        Log.w(TAG, "CREATE String: " + TodoTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertTodo(String text) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoTable.ITEM_TEXT, text);

        return getWritableDatabase().insert(TodoTable.TABLE_NAME, null, contentValues);
    }

    public List<TodoItem> retrieveTodos() {
        List<TodoItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor query = db.query(
                TodoTable.TABLE_NAME,
                new String[]{TodoTable._ID, TodoTable.ITEM_TEXT},
                null, null, null, null, null);

        while (query.moveToNext()) {
            long id = query.getLong(query.getColumnIndex(TodoTable._ID));
            String text = query.getString(query.getColumnIndex(TodoTable.ITEM_TEXT));

            TodoItem todoItem = new TodoItem(id, text);
            items.add(todoItem);
        }

        query.close();

        return items;
    }

    public boolean deleteTodo(Long todoId) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(TodoTable.TABLE_NAME, TodoTable._ID + "=?", new String[]{String.valueOf(todoId)});

        return delete > 0;
    }

    public boolean updateTodo(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TodoTable.ITEM_TEXT, todoItem.getText());
        int updated = db.update(TodoTable.TABLE_NAME,
                values,
                TodoTable._ID + "=?",
                new String[]{String.valueOf(todoItem.getItemId())}
        );

        return updated == 1;
    }

    public static class TodoTable implements BaseColumns {
        public static String TABLE_NAME = "ITEMS";
        public static String ITEM_TEXT = "TEXT";
        public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY, " +
                ITEM_TEXT + " TEXT);";
    }
}
