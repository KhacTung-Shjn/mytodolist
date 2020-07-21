package com.example.mytodolist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.mytodolist.AddPlan.FragmentAdd;
import com.example.mytodolist.HistoryPlan.FragmentHistory;
import com.example.mytodolist.PlanPlan.FragmentPlan;
import com.example.mytodolist.Service.NotificationService;
import com.example.mytodolist.SettingPlan.FragmentSetting;
import com.example.mytodolist.SettingPlan.GuiDuLieu;
import com.example.mytodolist.TodayPlan.FragmentToday;
import com.example.mytodolist.db.Db;
import com.example.mytodolist.entity.ToDoList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GuiDuLieu {
    Context context;
    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    ConstraintLayout constraintLayout;

    SharedPreferences sp;
    SharedPreferences sp1;
    SharedPreferences.Editor editor;
    int stt;
    String ktra;

    List<ToDoList> listTodo = new ArrayList<>();
    ArrayList<ToDoList> listFinal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sp1 = getSharedPreferences("language", MODE_PRIVATE);
        ktra = sp1.getString("ngonngu", "en");
        // thay doi ngon ngu he thong trong app
        Resources resources = getResources();
        Locale locale = new Locale(ktra);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());

        // chuong trinh bat dau
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        constraintLayout = findViewById(R.id.container);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new FragmentToday(context));
        fragmentTransaction.commit();

        //thay hinh nen
        sp = getSharedPreferences("getbackground", MODE_PRIVATE);
        new AsyncTask().execute(getBack());

        //thongbao

        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_today: {
                    fragment = new FragmentToday(context);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                    return true;
                }
                case R.id.navigation_plan: {
                    fragment = new FragmentPlan(context);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                    return true;
                }
                case R.id.navigation_add: {
                    fragment = new FragmentAdd(context);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                    return true;
                }
                case R.id.navigation_history: {
                    fragment = new FragmentHistory(context);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                    return true;
                }
                case R.id.navigation_setting: {
                    fragment = new FragmentSetting(context);
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                    return true;
                }
            }
            return false;
        }
    };


    @Override
    public void guiDuLieu(int thamso) {
        editor = sp.edit();
        editor.putInt("hinhnen", thamso);
        editor.commit();
        int background = 0;
        switch (thamso) {
            case 0: {
                background = R.drawable.hinhnen0;
                break;
            }
            case 1: {
                background = R.drawable.hinhnen1;
                break;
            }
            case 2: {
                background = R.drawable.hinhnen2;
                break;
            }
            case 3: {
                background = R.drawable.hinhnen3;
                break;
            }
            case 4: {
                background = R.drawable.hinhnen4;
                break;
            }
            case 5: {
                background = R.drawable.hinhnen5;
                break;
            }
        }
        constraintLayout.setBackgroundResource(background);
    }

    public int getBack() {
        int selected = sp.getInt("hinhnen", 0);
        return selected;
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

    public class AsyncTask extends android.os.AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            int back = integers[0];
            switch (back) {
                case 0: {
                    stt = R.drawable.hinhnen0;
                    break;
                }
                case 1: {
                    stt = R.drawable.hinhnen1;
                    break;
                }
                case 2: {
                    stt = R.drawable.hinhnen2;
                    break;
                }
                case 3: {
                    stt = R.drawable.hinhnen3;
                    break;
                }
                case 4: {
                    stt = R.drawable.hinhnen4;
                    break;
                }
                case 5: {
                    stt = R.drawable.hinhnen5;
                    break;
                }
            }
            return stt;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            constraintLayout.setBackgroundResource(integer);
        }
    }

}
