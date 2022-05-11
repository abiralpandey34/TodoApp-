package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddTodoActivity extends AppCompatActivity {

    Button saveButton, deleteButton;
    EditText todoTitle, todoDescription;
    RoomDB database;

    Dialog dialog;

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
            saveButton.setText("UPDATE");
        }
        catch (Exception e){
            todo = new Todo();
        }

        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String title = todoTitle.getText().toString();
                String description = todoDescription.getText().toString();

                //Checks if Title is empty or not
                if(todoTitle.getText().toString().trim().length()==0){
                    showAlertDialogPopup();
                    return;
                }

                if(todo.getTitle()==null){
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
                deleteConfirmationDialogBoxPopup(todo);
            }
        });


    }

    public void deleteConfirmationDialogBoxPopup(Todo todo){

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_confirmation_dialogbox);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Confirm = dialog.findViewById(R.id.btn_confirm);
        Button Cancel = dialog.findViewById(R.id.btn_cancel);

        dialog.show();

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.todoDao().delete(todo);
                finish();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void showAlertDialogPopup(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_message_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }

        Log.d("hello", "This is my message");

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button okButton = dialog.findViewById(R.id.ok_button);

        dialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }



}