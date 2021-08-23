package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 1;
    List<String> items;
    Button addButton;
    EditText inputItem;
    RecyclerView flatList;
    ItemsAdaptor itemsAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.addButton);
        inputItem = findViewById(R.id.inputItem);
        flatList = findViewById(R.id.flatList);

        loadItems();


        ItemsAdaptor.OnLongClickListener onLongClickListener = new ItemsAdaptor.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdaptor.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemsAdaptor.OnClickListener onClickListener = new ItemsAdaptor.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Toast.makeText(getApplicationContext(), "Item tapped " + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
       itemsAdaptor = new ItemsAdaptor(items, onLongClickListener, onClickListener);
        flatList.setAdapter(itemsAdaptor);
        flatList.setLayoutManager(new LinearLayoutManager(this));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = inputItem.getText().toString();
                items.add(todoItem);
                itemsAdaptor.notifyItemInserted(items.size() -1);
                inputItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(position, itemText);
            itemsAdaptor.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Warning!", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile () {
        return new File(getFilesDir(), "data.txt");
    }
    private void loadItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error Adding Items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing Items", e);
        }
    }
}