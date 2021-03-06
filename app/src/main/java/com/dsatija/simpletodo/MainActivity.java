package com.dsatija.simpletodo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dsatija.simpletodo.db.TaskContract;
import com.dsatija.simpletodo.db.TaskDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditNameDialogFragment.EditTaskDialogListener {
    ArrayList<Task> items;
    ArrayList<String> checkedValue = new ArrayList<>();
    TaskAdapter adapter;
    ListView lvItems;
    // private final int REQUEST_CODE = 10;
    private TaskDbHelper mHelper;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new TaskDbHelper(this);
        lvItems = (ListView) findViewById(R.id.lvItems);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry.COL_TASK_ID,
                        TaskContract.TaskEntry.COL_TASK_TITLE,
                        TaskContract.TaskEntry.COL_TASK_STATUS,
                        TaskContract.TaskEntry.COL_TASK_YEAR,
                        TaskContract.TaskEntry.COL_TASK_MONTH,
                        TaskContract.TaskEntry.COL_TASK_DAY,
                        TaskContract.TaskEntry.COL_TASK_PRIORITY,
                },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            Log.d(TAG, "Task: " + cursor.getString(idx));
            int idx2 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_STATUS);
            Log.d(TAG, "Status: " + cursor.getInt(idx2));
        }
        cursor.close();
        db.close();
        updateUI();
        setupListViewListener();
        setupOnClickListener();
    }

    private void showEditDialog(String taskName, int position, int year, int month, int day, String
            priority) {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance
                (taskName,
                        position, year, month, day, priority);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    private void setupOnClickListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox1);
                TextView tv = (TextView) view.findViewById(R.id.tvtaskName);
                //cb.performClick();
                if (cb.isChecked()) {
                    checkedValue.add(tv.getText().toString());

                } else if (!cb.isChecked()) {
                    checkedValue.remove(tv.getText().toString());
                }
                showEditDialog(items.get(position).toString(), position, items.get(position).year,
                        items.get(position).month, items.get(position).day,
                        items.get(position).getPriority());
                //showDialog(view);
                /*Intent i = new Intent(getApplicationContext(), EditItemActivity.class);
                i.putExtra("item",items.get(position).toString());
                i.putExtra("position",position);
                i.putExtra("year", items.get(position).year);
                i.putExtra("month", items.get(position).month);
                i.putExtra("day", items.get(position).day);
                startActivityForResult(i, REQUEST_CODE);
                */
            }
        });
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View view, int pos, long id) {
                        //String task ;
                        if (items.size() == 1) {
                            //task = items.get(items.size() - 1).toString();
                            Log.d(TAG, "Task to remove: " +
                                    items.get(items.size() - 1));
                        } else {
                            //task = items.get(pos).toString();
                            Log.d(TAG, "Task to remove: " + items.get(pos));

                        }
                        id = items.get(pos).getId();
                        Log.d(TAG, "id value" + String.valueOf(id));
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        int y = db.delete(TaskContract.TaskEntry.TABLE,
                                TaskContract.TaskEntry.COL_TASK_ID + " = ?",
                                new String[]{String.valueOf(items.get(pos).getId())});
                        db.close();
                        updateUI();
                        Log.d(TAG, "calling update" + String.valueOf(y));
                        return true;
                    }
                });
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItems);
        String itemText = etNewItem.getText().toString();
        etNewItem.setText("");
        Log.d(TAG, "Task to add: " + itemText);
        if (itemText != null && !itemText.isEmpty()) {
            SQLiteDatabase db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, itemText);
            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
            updateUI();
        }

    }

    private void updateUI() {
        Log.d(TAG, "Inside updateui ");
        lvItems.setAdapter(adapter);
        ArrayList<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{
                        TaskContract.TaskEntry.COL_TASK_ID,
                        TaskContract.TaskEntry.COL_TASK_TITLE,
                        TaskContract.TaskEntry.COL_TASK_STATUS,
                        TaskContract.TaskEntry.COL_TASK_YEAR,
                        TaskContract.TaskEntry.COL_TASK_MONTH,
                        TaskContract.TaskEntry.COL_TASK_DAY,
                        TaskContract.TaskEntry.COL_TASK_PRIORITY},
                null, null, null, null, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        if (cursor.getCount() >= 0) {
            while (cursor.moveToNext()) {
                int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_ID);
                int idx1 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
                int idx2 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_STATUS);
                int idx3 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_YEAR);
                int idx4 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_MONTH);
                int idx5 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DAY);
                int idx6 = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_PRIORITY);
                Task task = new Task(cursor.getString(idx1), cursor.getInt(idx2),
                        cursor.getInt(idx3), cursor.getInt(idx4), cursor.getInt(idx5),
                        cursor.getString(idx6));
                task.setId(cursor.getInt(idx));
                taskList.add(task);
            }
            if (adapter == null) {
                adapter = new TaskAdapter(this, taskList, new TaskDbHelper(this));
                Log.d(TAG, "adapter null");
                lvItems.setAdapter(adapter);
            } else {
                adapter.clear();
                Log.d(TAG, String.valueOf(taskList));
                adapter.addAll(taskList);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "calling notify");
            }
            cursor.close();
            db.close();
            Log.d(TAG, String.valueOf(taskList));
            items = taskList;
        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK ) {
            //int pos = data.getExtras().getInt("pos");
            String editted_task =data.getStringExtra("edittextvalue");
            int pos=data.getIntExtra("position",0);
            Log.d(TAG, "Editted task: " + editted_task);
            //Log.d(TAG,"Replacing:" + items.get(pos));
            int year = data.getIntExtra("year", 0);
            int month = data.getIntExtra("month", 0);
            int day = data.getIntExtra("day", 0);
            Task task = items.get(pos);
            String oldTaskName=items.get(pos).toString();
            if (editted_task != null && !editted_task.isEmpty()) {
                task.taskName=editted_task;
                task.year=year;
                task.month=month;
                task.day=day;
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, editted_task);
                values.put(TaskContract.TaskEntry.COL_TASK_YEAR,year);
                values.put(TaskContract.TaskEntry.COL_TASK_MONTH,month);
                values.put(TaskContract.TaskEntry.COL_TASK_DAY,day);
                //add values
                db.update(TaskContract.TaskEntry.TABLE, values,
                        TaskContract.TaskEntry.COL_TASK_TITLE
                        + " = ?", new String[]{oldTaskName});
                db.close();
                updateUI();
            }
        }
    }
    */

    @Override
    public void onFinishEditDialog(String inputText, int position, int year, int month, int day,
                                   String priority) {
        if (inputText != null) {
            Log.d(TAG, "Editted task: " + inputText);
            Task task = items.get(position);
            String oldTaskName = items.get(position).toString();
            if (inputText != null && !inputText.isEmpty()) {
                task.taskName = inputText;
                task.year = year;
                task.month = month;
                task.day = day;
                task.setPriority(priority);
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                // values.put(TaskContract.TaskEntry.COL_TASK_ID, task.getId());
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, inputText);
                values.put(TaskContract.TaskEntry.COL_TASK_YEAR, year);
                values.put(TaskContract.TaskEntry.COL_TASK_MONTH, month);
                values.put(TaskContract.TaskEntry.COL_TASK_DAY, day);
                values.put(TaskContract.TaskEntry.COL_TASK_PRIORITY, task.getPriority());
                Log.d(TAG, String.valueOf(task.getId()));
                //add values
                db.update(TaskContract.TaskEntry.TABLE, values,
                        TaskContract.TaskEntry.COL_TASK_ID
                                + " = ?", new String[]{String.valueOf(task.getId())});
                db.close();
                updateUI();
            }
        }

    }
}