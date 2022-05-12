package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    private List<Todo> todoList = new ArrayList<Todo>();
    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    RoomDB database;

    FloatingActionButton addItemButton;
    ImageView noTodoItem;
    TextView noTodoText;
    Dialog dialog;


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
    public void onBackPressed() {

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

        //Changing only the text of another component for re-usability.
        TextView Text = dialog.findViewById(R.id.textView);
        Text.setText("Are you sure you want to exit?");

        //Changing Icon
        ImageView exitImage = dialog.findViewById(R.id.imageView);
        exitImage.setVisibility(View.GONE);

        dialog.show();

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

    private void confirmDialog(Context context){

        final AlertDialog alert = new AlertDialog.Builder(
                new ContextThemeWrapper(context,android.R.style.Theme_Dialog))
                .create();
        alert.setTitle("Alert");
        alert.setMessage("Do you want to exit ?");
        alert.setIcon(R.drawable.not_allowed);
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        alert.dismiss();

                        finish();

                    }
                });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        alert.dismiss();

                    }
                });

        alert.show();
    }
}