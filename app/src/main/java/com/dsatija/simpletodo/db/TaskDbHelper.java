package com.dsatija.simpletodo.db;

/**
 * Created by disha_000 on 9/19/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dsatija.simpletodo.Task;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL," +
                TaskContract.TaskEntry.COL_TASK_STATUS + " INTEGER," +
                TaskContract.TaskEntry.COL_TASK_YEAR + " INTEGER," +
                TaskContract.TaskEntry.COL_TASK_MONTH + " INTEGER," +
                TaskContract.TaskEntry.COL_TASK_DAY + " INTEGER" +
                ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }

    /*public void updateTask(Task changedTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE , changedTask.taskName);
        values.put(TaskContract.TaskEntry.COL_TASK_STATUS, changedTask.getStatus());
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE , changedTask.year);
        values.put(TaskContract.TaskEntry.COL_TASK_STATUS, changedTask.month);
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE , changedTask.day);
       // db.update(TaskContract.TaskEntry.TABLE, values, TaskContract.TaskEntry.COL_TASK_TITLE +
       //         " = ?", new String[]{String.valueOf(changedTask.taskName)});

    db.update(   databaseHelper.updateItem(itemId, itemText, year, month, day);)
    }*/

    public void updateTask(Task changedTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TaskContract.TaskEntry.COL_TASK_TITLE , changedTask.taskName);
        values.put(TaskContract.TaskEntry.COL_TASK_STATUS, changedTask.getStatus());
        values.put(TaskContract.TaskEntry.COL_TASK_YEAR , changedTask.year);
        values.put(TaskContract.TaskEntry.COL_TASK_MONTH, changedTask.month);
        values.put(TaskContract.TaskEntry.COL_TASK_DAY , changedTask.day);

        db.update(TaskContract.TaskEntry.TABLE, values, TaskContract.TaskEntry.COL_TASK_TITLE +
                " = ?", new String[]{String.valueOf(changedTask.taskName)});

        db.close();
    }
}