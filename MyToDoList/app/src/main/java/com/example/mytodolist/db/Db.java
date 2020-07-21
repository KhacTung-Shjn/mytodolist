package com.example.mytodolist.db;

import androidx.room.Room;

import com.example.mytodolist.App;

public class Db {

    private static Appdatabase db;

    public static Appdatabase get() {
        if (db == null) {
            db = Room.databaseBuilder(App.INSTANT, Appdatabase.class, "ToDoList")
                    .allowMainThreadQueries()
                    //.fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }
}
