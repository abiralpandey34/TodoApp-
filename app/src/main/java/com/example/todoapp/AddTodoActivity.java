package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddTodoActivity extends AppCompatActivity {

    Button saveButton, deleteButton;
    EditText todoTitle, todoDescription;
    RoomDB database;

    Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        saveButton = findViewById(R.id.add_item_button);
        deleteButton = findViewById(R.id.delete_item_button);
        deleteButton.setVisibility(View.GONE);

        todoTitle = findViewById(R.id.todo_title);
        todoDescription = findViewById(R.id.todo_description);

        // create db instance
        database = RoomDB.getInstance(this);

        try {
            Intent intent = getIntent();
            todo = (Todo) intent.getSerializableExtra("TODO_CLASS");

            // set old data
            todoTitle.setText(todo.getTitle());
            todoDescription.setText(todo.getDescription());
            deleteButton.setVisibility(View.VISIBLE);
        }
        catch (Exception e){
            todo = new Todo();
        }

        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String title = todoTitle.getText().toString();
                String description = todoDescription.getText().toString();

                if(todo.getTitle().isEmpty()){
                    // add todo to db
                    database.todoDao().insertTodo(new Todo(title, description, new Date().toString(), false));
                    Toast.makeText(AddTodoActivity.this, "Todo Created", Toast.LENGTH_SHORT).show();
                }
                else{
                    todo.setTitle(title);
                    todo.setDescription(description);
                    todo.setCompleted(false);

                    // to update todo
                    database.todoDao().update(todo);
                    Toast.makeText(AddTodoActivity.this, "Todo updated", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                database.todoDao().delete(todo);
                finish();
            }
        });
    }



}