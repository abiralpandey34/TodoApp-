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


//class Splash extends Activity {
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle icicle) {
//        super.onCreate(icicle);
//        setContentView(R.layout.loading_screen);
//
//        /* New Handler to start the Menu-Activity
//         * and close this Splash-Screen after some seconds.*/
//
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//                Intent mainIntent = new Intent(Splash.this, MainActivity.class);
//                Splash.this.startActivity(mainIntent);
//                Splash.this.finish();
//            }
//        }, 4000);
//    }
//}

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    private List<Todo> todoList = new ArrayList<Todo>();
    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    RoomDB database;

    FloatingActionButton addItemButton;
    ImageView noTodoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.list);
        addItemButton = findViewById(R.id.add_item_button);

        adapter = new TodoAdapter(todoList, this);
        recyclerView.setAdapter(adapter);

        database = RoomDB.getInstance(this);
        this.todoList.addAll(this.database.todoDao().getAllTodos());

        if(!todoList.isEmpty()){
            noTodoItem = findViewById(R.id.no_todo_image);
            noTodoItem.setVisibility(View.GONE);
        }

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

        //todo : When note is deleted and ultimately no note is present, image doesn't show. Works at startup
        if(!todoList.isEmpty()){
            noTodoItem = findViewById(R.id.no_todo_image);
            noTodoItem.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
    }
}