package com.example.mytodolist.TodayPlan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytodolist.HistoryPlan.FragmentHistory;
import com.example.mytodolist.PlanPlan.FragmentPlan;
import com.example.mytodolist.R;
import com.example.mytodolist.db.Db;
import com.example.mytodolist.entity.ToDoList;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fragment_Detail_Plan_Today extends Fragment implements View.OnClickListener {

    ToDoList toDoList;
    Button btnClose, btnRemove, btnModify;
    Button btnNgayDetail, btnGioDetail, btnLevelDetail, btnAlarmDetail;
    EditText etContentDetail;
    Context context;
    int ngay, thang, nam, gio, phut;
    String[] dateInfo, timeInfo;
    Snackbar snackbar;
    String kiemtraPlan;

    public Fragment_Detail_Plan_Today(ToDoList toDoList, Context context, String kiemtraPlan) {
        this.toDoList = toDoList;
        this.context = context;
        this.kiemtraPlan = kiemtraPlan;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail_today, container, false);

        btnModify = view.findViewById(R.id.btnModify);
        btnRemove = view.findViewById(R.id.btnRemove);
        btnClose = view.findViewById(R.id.btnClose);
        btnNgayDetail = view.findViewById(R.id.btnNgay);
        btnGioDetail = view.findViewById(R.id.btnGio);
        btnLevelDetail = view.findViewById(R.id.btnLevel);
        btnAlarmDetail = view.findViewById(R.id.btnAlarm);
        etContentDetail = view.findViewById(R.id.etPlan);

        btnNgayDetail.setText(toDoList.ngay);
        btnGioDetail.setText(toDoList.gio);
        btnLevelDetail.setText(toDoList.capdo);
        btnAlarmDetail.setText(toDoList.alarm);
        etContentDetail.setText(toDoList.content);


        btnClose.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        btnModify.setOnClickListener(this);

        btnNgayDetail.setOnClickListener(this);
        btnGioDetail.setOnClickListener(this);
        btnLevelDetail.setOnClickListener(this);
        btnAlarmDetail.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRemove: {
                ToDoList toDoListRemove = new ToDoList();
                toDoListRemove.id = toDoList.id;
                Db.get().toDoListDAO().delete(toDoListRemove);

                snackbar = Snackbar.make(btnClose, R.string.snackbar_remove, Snackbar.LENGTH_SHORT);
                snackbar.show();
                switch (kiemtraPlan) {
                    case "today": {
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentToday(context)).commit();
                        break;
                    }
                    case "plan": {
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentPlan(context)).commit();
                        break;
                    }
                    case "history": {
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentHistory(context)).commit();
                        break;
                    }
                }
                break;
            }
            case R.id.btnModify: {

                if (etContentDetail.getText().toString().trim().length() != 0) {
                    String dayDb = btnNgayDetail.getText().toString();
                    String gioDb = btnGioDetail.getText().toString();
                    String levelDb = btnLevelDetail.getText().toString();
                    String alarmDb = btnAlarmDetail.getText().toString();
                    String contenDb = etContentDetail.getText().toString();
                    ToDoList toDoListUpdate = new ToDoList(dayDb, gioDb, levelDb, alarmDb, contenDb);
                    toDoListUpdate.id = toDoList.id;
                    Db.get().toDoListDAO().update(toDoListUpdate);

                    //thong bao
                    snackbar = Snackbar.make(btnClose, R.string.snackbar_modify, Snackbar.LENGTH_SHORT);

                } else {
                    snackbar = Snackbar.make(btnClose, R.string.hint_edit_text, Snackbar.LENGTH_SHORT);
                }
                snackbar.show();
                switch (kiemtraPlan) {
                    case "today": {
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentToday(context)).commit();
                        break;
                    }
                    case "plan": {
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentPlan(context)).commit();
                        break;
                    }
                    case "history": {
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentHistory(context)).commit();
                        break;
                    }
                }
                break;
            }
            case R.id.btnClose: {
                switch (kiemtraPlan) {
                    case "today": {
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentToday(context)).commit();
                        break;
                    }
                    case "plan": {
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentPlan(context)).commit();
                        break;
                    }
                    case "history": {
                        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentHistory(context)).commit();
                        break;
                    }
                }
                break;
            }
            case R.id.btnNgay: {
                dateInfo = getInfoDate();
                nam = Integer.parseInt(dateInfo[2]);
                thang = Integer.parseInt(dateInfo[1]);
                ngay = Integer.parseInt(dateInfo[0]);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String result = formatDate(dayOfMonth, month + 1, year);
                        btnNgayDetail.setText(result);
                    }
                }, nam, (thang - 1), ngay);
                datePickerDialog.show();
                break;
            }
            case R.id.btnGio: {
                timeInfo = getInfoTime();
                gio = Integer.parseInt(timeInfo[0]);
                phut = Integer.parseInt(timeInfo[1]);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String result = formatTime(hourOfDay, minute);
                        btnGioDetail.setText(result);
                    }
                }, gio, phut, true);
                timePickerDialog.show();
                break;
            }
            case R.id.btnLevel: {
                PopupMenu popup = new PopupMenu(context, btnLevelDetail);
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.getMenuInflater().inflate(R.menu.menu_level, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        btnLevelDetail.setText(item.getTitle());
                        return true;
                    }
                });
                popup.show();
                break;
            }
            case R.id.btnAlarm: {
                PopupMenu popup = new PopupMenu(context, btnAlarmDetail);
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.getMenuInflater().inflate(R.menu.menu_alarm, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        btnAlarmDetail.setText(item.getTitle());
                        return true;
                    }
                });
                popup.show();
                break;
            }
        }
    }

    public String[] getInfoDate() {
        String date = btnNgayDetail.getText().toString();
        String[] s = date.split("/");
        return s;
    }

    public String[] getInfoTime() {
        String date = btnGioDetail.getText().toString();
        String[] s = date.split(":");
        return s;
    }

    public String formatDate(int ngay, int thang, int nam) {
        String date = ngay + "/" + thang + "/" + nam;
        String result = "";
        SimpleDateFormat dt = new SimpleDateFormat("dd/mm/yyyy");
        try {
            Date d = dt.parse(date);
            result = dt.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String formatTime(int gio, int phut) {
        String time = gio + ":" + phut;
        String result = "";
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");

        try {
            Date t = tf.parse(time);
            result = tf.format(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
