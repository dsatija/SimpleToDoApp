package com.dsatija.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dsatija.simpletodo.db.TaskDbHelper;

import java.util.ArrayList;

/**
 * Created by disha_000 on 9/22/2016.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    private static final String TAG2 = "TaskAdapter";
    boolean[] itemChecked;
    private TaskDbHelper db;

    public TaskAdapter(Context context, ArrayList<Task> tasks, TaskDbHelper dbHelper) {
        super(context, 0, tasks);
        itemChecked = new boolean[100];
        this.db = dbHelper;
    }

    private class ViewHolder {
        TextView tvName;
        TextView taskDate;
        CheckBox ck1;
        TextView tvPriority;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        // Get the data item for this position
        final Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent,
                    false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvtaskName);
            holder.ck1 = (CheckBox) convertView.findViewById(R.id.checkBox1);
            holder.taskDate = (TextView) convertView.findViewById(R.id.textView2);
            holder.tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(task.taskName);
        if (task.getPriority() != null) {
            holder.tvPriority.setText(task.getPriority());
            switch (task.getPriority()) {
                case "High":
                    holder.tvPriority.setBackgroundColor(Color.RED);
                    break;
                case "Medium":
                    holder.tvPriority.setBackgroundColor(Color.parseColor("#ffcc00"));
                    break;
                case "Low":
                    holder.tvPriority.setBackgroundColor(Color.parseColor("#458B00"));
            }

        }
        holder.ck1.setChecked(false);
        if (task.year == 0 && task.month == 0 && task.day == 0) {
            holder.taskDate.setText("");
        } else {
            //Log.d(TAG2, String.valueOf(task.year + task.month + task.day));
            holder.taskDate.setText(("Due By:" + task.year + "-" + ((task.month < 10) ? "0" + task.month :
                    task.month) + "-" + ((task.day < 10) ? "0" + task.day : task.day)));
        }
        if (task.getStatus() == 1)
            holder.ck1.setChecked(true);
        else
            holder.ck1.setChecked(false);
        holder.ck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Task changedTask = new Task((String) holder.tvName.getText(),
                        cb.isChecked() ? 1 : 0, task.year, task.month, task.day,
                        (String) holder.tvPriority.getText());
                changedTask.setId(task.getId());
                // changedTask.setStatus(cb.isChecked()==true?1:0);
                db.updateTask(changedTask);
                if (holder.ck1.isChecked())
                    itemChecked[position] = true;
                else
                    itemChecked[position] = false;
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}