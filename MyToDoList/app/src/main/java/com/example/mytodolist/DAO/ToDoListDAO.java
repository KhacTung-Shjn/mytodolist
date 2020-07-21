package com.example.mytodolist.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mytodolist.entity.ToDoList;


import java.util.List;


@Dao
public interface ToDoListDAO {

    @Insert
    void insert(ToDoList toDoList);

    @Update
    void update(ToDoList toDoList);

    @Delete
    void delete(ToDoList toDoList);

    @Query("select * from ToDoList")
    List<ToDoList> getAll();
}
