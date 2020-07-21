package com.example.mytodolist.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mytodolist.App;
import com.example.mytodolist.MainActivity;
import com.example.mytodolist.R;
import com.example.mytodolist.db.Db;
import com.example.mytodolist.entity.ToDoList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationService extends Service {
    List<ToDoList> listTodo = new ArrayList<>();
    ArrayList<ToDoList> listFinal = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Luong().start();
        return super.onStartCommand(intent, flags, startId);
    }

    public class Luong extends Thread {
        public void run() {
            while (true) {
                listTodo = Db.get().toDoListDAO().getAll();
                String current = getCurrenDateTime();

                for (ToDoList t : listTodo) {
                    if (soSanhDate(current, t.ngay) == 0) {
                        listFinal.add(t);
                    }
                }
                for (ToDoList t : listFinal) {
                    if (t.alarm.equals("Yes")) {
                        if (t.gio.equals(getCurrentTime())) {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), App.CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                    .setContentTitle("ThongBao")
                                    .setContentText("Alo co thong bao kia !")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT); // ưu tiên để mặc định
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
                            notificationManager.notify(1, builder.build());
                        }
                    }
                }
                listFinal.clear();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int gio, phut;
        gio = calendar.get(Calendar.HOUR_OF_DAY);
        phut = calendar.get(Calendar.MINUTE);

        String time = formatTime(gio, phut);
        return time;
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

    public int soSanhDate(String date1, String date2) {
        String[] s1 = date1.split("/");
        String[] s2 = date2.split("/");
        //1 la lon
        // 0 la bang
        //-1 la be
        //2 la loi
        int ktraNam = Integer.parseInt(s1[2]) - Integer.parseInt(s2[2]);
        int ktraThang = Integer.parseInt(s1[1]) - Integer.parseInt(s2[1]);
        int ktraNgay = Integer.parseInt(s1[0]) - Integer.parseInt(s2[0]);

        if (ktraNam == 0) {
            if (ktraThang == 0) {
                if (ktraNgay == 0) {
                    return 0;
                } else if (ktraNgay > 0) {
                    return 1;
                } else if (ktraNgay < 0) {
                    return -1;
                }
            } else if (ktraThang > 0) {
                return 1;
            } else if (ktraThang < 0) {
                return -1;
            }
        } else if (ktraNam > 0) {
            return 1;
        } else if (ktraNam < 0) {
            return -1;
        }
        return 2;
    }

    public String getCurrenDateTime() {
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        String dateTime = formatDate(ngay, thang + 1, nam);

        return dateTime;
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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
