package com.example.mytodolist.TodayPlan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.R;
import com.example.mytodolist.db.Db;
import com.example.mytodolist.entity.ToDoList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentToday extends Fragment implements ItemClickListener {
    Context context;
    TextView tvToday, tvMaxim;
    RecyclerView recyclerView;
    List<ToDoList> listTodo = new ArrayList<>();
    ArrayList<ToDoList> listFinal = new ArrayList<>();
    ToDoListAdapter toDoListAdapter;
    LinearLayoutManager layoutManager;

    public FragmentToday(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listTodo = Db.get().toDoListDAO().getAll();
        String current = getCurrenDateTime();

        for (ToDoList t : listTodo) {
            if (soSanhDate(current, t.ngay) == 0) {
                listFinal.add(t);
            }
        }
        toDoListAdapter = new ToDoListAdapter(listFinal, context, "today");
        toDoListAdapter.setItemClickListener(this);
        layoutManager = new LinearLayoutManager(context);

    }

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        tvToday = view.findViewById(R.id.tvToday);
        tvMaxim = view.findViewById(R.id.tvMaxim);
        recyclerView = view.findViewById(R.id.rcvList);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(toDoListAdapter);

        return view;
    }


    @Override
    public void onItemClick(View view, int position) {
        ToDoList toDoList = listFinal.get(position);
        Log.d("ID", toDoList.id + "");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new Fragment_Detail_Plan_Today(toDoList, context, "today"));
        fragmentTransaction.commit();
    }

    @Override
    public void onSwitchClick(View view, int position, boolean checked) {
        ToDoList toDoList = listFinal.get(position);
        String check;
        ToDoList toDoListUpdate = new ToDoList();
        if (checked) {
            toDoListUpdate.alarm = "Yes";
        } else {
            toDoListUpdate.alarm = "No";
        }
        toDoListUpdate = new ToDoList(toDoList.ngay, toDoList.gio, toDoList.capdo, toDoListUpdate.alarm, toDoList.content);
        toDoListUpdate.id = toDoList.id;
        Db.get().toDoListDAO().update(toDoListUpdate);

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

}
