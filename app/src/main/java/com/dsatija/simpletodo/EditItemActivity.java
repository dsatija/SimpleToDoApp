package com.dsatija.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {
   // private static final String TAG = "EditItemActivity";
    String item;
    int position;
    EditText etEditNew;
    EditText etEditted ;
    private TextView textDate;
    private DatePicker dpDueDate;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        setContentView(R.layout.activity_edit_item);
        item = getIntent().getStringExtra("item");
        position = getIntent().getIntExtra("position",0);

        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        day = getIntent().getIntExtra("day", 0);

        etEditted = (EditText) findViewById(R.id.etEdit);
        etEditted.requestFocus();
        etEditted.setText(item);
        etEditted.setSelection(etEditted.getText().length());
        if (year == 0 || day == 0) {
            setDate(currentYear, currentMonth, currentDay);
        }
        else {
            setDate(year, month, day);
        }

    }

    public void onSave(View view) {
        Intent intent = new Intent();
        etEditNew = (EditText) findViewById(R.id.etEdit);
        Intent edittextvalue = intent.putExtra("edittextvalue", etEditNew.getText().toString());
        //Log.d(TAG, "onSave:" + etEditNew.getText().toString() );
        intent.putExtra("position",position);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setDate(int newYear, int newMonth, int newDay) {
        dpDueDate = (DatePicker) findViewById(R.id.dpDueDate);
        year = newYear;
        month = newMonth;
        day = newDay;
        dpDueDate.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int newYear, int newMonth, int newDay) {
                year = newYear;
                month = newMonth + 1;
                day = newDay;
            }
        });
    }

}

