package com.example.mytodolist.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class ToDoList {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String ngay;
    public String gio;
    public String capdo;
    public String alarm;
    public String content;

    public ToDoList() {
    }

    public ToDoList(String ngay, String gio, String capdo, String alarm, String content) {
        this.ngay = ngay;
        this.gio = gio;
        this.capdo = capdo;
        this.alarm = alarm;
        this.content = content;
    }
}
