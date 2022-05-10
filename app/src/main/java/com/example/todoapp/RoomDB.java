package com.example.todoapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Todo.class}, version = 1, exportSchema=false)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB db;
    private static String databaseName = "Todo";

    public synchronized static RoomDB getInstance(Context context){
        if(db==null){
            db = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, databaseName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return db;
    }

    public abstract TodoDao todoDao();

}
