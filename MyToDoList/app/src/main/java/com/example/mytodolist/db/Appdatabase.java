package com.example.mytodolist.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mytodolist.DAO.ToDoListDAO;
import com.example.mytodolist.entity.ToDoList;

@Database(entities = {ToDoList.class}, version = 1)
//khi them cot trong entity thi db se bi thay doi => update version và phải có thêm migrate(giữ data cũ)
// hoặc .fallbackToDestructiveMigration() (huy data cũ)
public abstract class Appdatabase extends RoomDatabase {

    public abstract ToDoListDAO toDoListDAO();
}
