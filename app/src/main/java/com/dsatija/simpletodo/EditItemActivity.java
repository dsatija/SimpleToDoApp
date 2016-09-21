package com.dsatija.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
   // private static final String TAG = "EditItemActivity";
    String item;
    int position;
    EditText etEditNew;
    EditText etEditted ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        item = getIntent().getStringExtra("item");
        position = getIntent().getIntExtra("position",0);
        etEditted = (EditText) findViewById(R.id.etEdit);
        etEditted.requestFocus();
        etEditted.setText(item);
        etEditted.setSelection(etEditted.getText().length());

    }

    public void onSave(View view) {
        Intent intent = new Intent();
        etEditNew = (EditText) findViewById(R.id.etEdit);
        Intent edittextvalue = intent.putExtra("edittextvalue", etEditNew.getText().toString());
        //Log.d(TAG, "onSave:" + etEditNew.getText().toString() );
        intent.putExtra("position",position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
