package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText editItem;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editItem = findViewById(R.id.editItem);
        saveButton = findViewById(R.id.saveButton);

        getSupportActionBar().setTitle("Edit item!");
        String s = getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT);
        editItem.setText(s);
        getIntent().getStringExtra(MainActivity.KEY_ITEM_POSITION);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra(MainActivity.KEY_ITEM_TEXT, editItem.getText().toString());
                i.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}