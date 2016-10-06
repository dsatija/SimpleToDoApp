package com.dsatija.simpletodo;
/**
 * Created by disha_000 on 9/30/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
// ...

public class EditNameDialogFragment extends DialogFragment implements View.OnClickListener {
    private EditText mEditText;
    private DatePicker dpDueDate;
    private Button saveButton;
    private int position;
    RadioGroup rgPriority;
    String oldTaskName;
    String old_priority;
    String new_priority;
    //private String Tag;

    public EditNameDialogFragment() {
        // Empty constructor is required for DialogFragment
    }

    public interface EditTaskDialogListener {
        void onFinishEditDialog(String inputText, int position, int year, int month, int day,
                                String priority);
    }

    public static EditNameDialogFragment newInstance(String taskName, int position, int year, int month,
                                                     int day, String priority) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", taskName);
        args.putInt("position", position);
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        args.putString("priority", priority);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.etEdit);
        saveButton = (Button) view.findViewById(R.id.btnTaskSave);
        oldTaskName = getArguments().getString("title");
        position = getArguments().getInt("position");
        old_priority = getArguments().getString("priority");
        mEditText.setText(getArguments().getString("title", "Enter Task"));
        mEditText.requestFocus();
        saveButton.setOnClickListener(this);
        ((RadioButton) view.findViewById(R.id.rb1)).setOnClickListener(this);
        ((RadioButton) view.findViewById(R.id.rb2)).setOnClickListener(this);
        ((RadioButton) view.findViewById(R.id.rb3)).setOnClickListener(this);
        //setting Radio Group
        rgPriority = (RadioGroup) view.findViewById(R.id.rgOptions);
        if (old_priority != null) {
            int selectedRadioBtn = getSelectedPriority(old_priority);
            if (selectedRadioBtn > 0) {
                ((RadioButton) view.findViewById(selectedRadioBtn)).setChecked(true);
            }
        }
        dpDueDate = (DatePicker) view.findViewById(R.id.editdpDueDate);
        if (getArguments().getInt("year") != 0) {
            dpDueDate.updateDate(getArguments().getInt("year"), getArguments().getInt("month") - 1,
                    getArguments().getInt("day"));
        }
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private int getSelectedPriority(String priority) {
        switch (priority.toLowerCase()) {
            case "low":
                return R.id.rb1;
            case "medium":
                return R.id.rb2;
            case "high":
                return R.id.rb3;
            default:
                return -1;
        }
    }

    public void onRadioButtonClicked(View view) {
        int selected = rgPriority.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) view.findViewById(selected);
        if (rb != null) {
            //Toast.makeText(this.getContext(),rb.getText(),Toast.LENGTH_SHORT).show();
            new_priority = rb.getText().toString();
        }
    }

    @Override
    public void onClick(View view) {
        onRadioButtonClicked(view);
        //Toast.makeText(this.getContext(),new_priority, Toast.LENGTH_SHORT).show();
        if (view == saveButton) {
            EditTaskDialogListener listener = (EditTaskDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString(), position, dpDueDate.getYear(),
                    dpDueDate.getMonth() + 1, dpDueDate.getDayOfMonth(), new_priority);
            dismiss();
        }
    }
}