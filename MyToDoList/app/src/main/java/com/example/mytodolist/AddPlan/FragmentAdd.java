package com.example.mytodolist.AddPlan;

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
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytodolist.R;
import com.example.mytodolist.db.Db;
import com.example.mytodolist.entity.ToDoList;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class FragmentAdd extends Fragment implements View.OnClickListener {
    Button btnSave, btnNgay, btnGio, btnLevel, btnAlarm;
    TextView tvTitle;
    EditText etContent;
    Context context;
    int ngay, thang, nam, gio, phut;
    String date, time;

    public FragmentAdd(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        ngay = calendar.get(Calendar.DAY_OF_MONTH);
        thang = calendar.get(Calendar.MONTH);
        nam = calendar.get(Calendar.YEAR);
        gio = calendar.get(Calendar.HOUR_OF_DAY);
        phut = calendar.get(Calendar.MINUTE);

        date = formatDate(ngay, thang + 1, nam);
        time = formatTime(gio, phut);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        btnSave = view.findViewById(R.id.btnSave);
        btnNgay = view.findViewById(R.id.btnNgay);
        btnGio = view.findViewById(R.id.btnGio);
        btnLevel = view.findViewById(R.id.btnLevel);
        btnAlarm = view.findViewById(R.id.btnAlarm);
        tvTitle = view.findViewById(R.id.tvTitle);
        etContent = view.findViewById(R.id.etPlan);

        btnNgay.setText(date);
        btnGio.setText(time);

        btnAlarm.setOnClickListener(this);
        btnNgay.setOnClickListener(this);
        btnGio.setOnClickListener(this);
        btnLevel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave: {
                Snackbar snackbar;
                if (etContent.getText().toString().trim().length() != 0) {
                    String dayDb = btnNgay.getText().toString();
                    String gioDb = btnGio.getText().toString();
                    String levelDb = btnLevel.getText().toString();
                    String alarmDb = btnAlarm.getText().toString();
                    String contenDb = etContent.getText().toString();

                    ToDoList toDoList = new ToDoList(dayDb, gioDb, levelDb, alarmDb, contenDb);
                    Db.get().toDoListDAO().insert(toDoList);

                    //thong bao
                    snackbar = Snackbar.make(btnSave, R.string.snackbar_success, Snackbar.LENGTH_SHORT);
                    // quay ve ban dau
                    btnNgay.setText(date);
                    btnGio.setText(time);
                    etContent.setText("");
                    btnLevel.setText(R.string.level1);
                    btnAlarm.setText(R.string.yes);
                } else {
                    snackbar = Snackbar.make(btnSave, R.string.hint_edit_text, Snackbar.LENGTH_SHORT);
                }
                snackbar.show();

                break;
            }
            case R.id.btnNgay: {

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String result = formatDate(dayOfMonth, month + 1, year);
                        btnNgay.setText(result);
                    }
                }, nam, thang, ngay);
                datePickerDialog.show();
                break;
            }
            case R.id.btnGio: {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String result = formatTime(hourOfDay, minute);
                        btnGio.setText(result);
                    }
                }, gio, phut, true);
                timePickerDialog.show();
                break;
            }
            case R.id.btnLevel: {
                PopupMenu popup = new PopupMenu(context, btnLevel);
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
                        btnLevel.setText(item.getTitle());
                        return true;
                    }
                });
                popup.show();
                break;
            }
            case R.id.btnAlarm: {
                PopupMenu popup = new PopupMenu(context, btnAlarm);
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
                        btnAlarm.setText(item.getTitle());
                        return true;
                    }
                });
                popup.show();
                break;
            }
        }
    }
}
