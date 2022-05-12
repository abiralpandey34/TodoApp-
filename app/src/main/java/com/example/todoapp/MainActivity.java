package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    private List<Todo> todoList = new ArrayList<Todo>();
    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    RoomDB database;

    FloatingActionButton addItemButton;
    ImageView noTodoItem;
    TextView noTodoText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noTodoItem = findViewById(R.id.no_todo_image);
        noTodoText = findViewById(R.id.no_todo_text);

        recyclerView = findViewById(R.id.list);
        addItemButton = findViewById(R.id.add_item_button);

        adapter = new TodoAdapter(todoList, this);
        recyclerView.setAdapter(adapter);

        database = RoomDB.getInstance(this);
        this.todoList.addAll(this.database.todoDao().getAllTodos());

        determineVisibility();

        addItemButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddTodoActivity.class);
                startActivity(addIntent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        this.todoList.clear();
        this.todoList.addAll(this.database.todoDao().getAllTodos());

        determineVisibility();

        adapter.notifyDataSetChanged();
    }

    public void determineVisibility(){

        if(!todoList.isEmpty()){

            noTodoItem.setVisibility(View.GONE);
            noTodoText.setVisibility(View.GONE);
        }
        else{
            noTodoItem.setVisibility(View.VISIBLE);
            noTodoText.setVisibility(View.VISIBLE);
        }
    }
}