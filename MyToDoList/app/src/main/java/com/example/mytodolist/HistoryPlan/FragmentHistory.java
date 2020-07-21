package com.example.mytodolist.HistoryPlan;

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
import com.example.mytodolist.TodayPlan.Fragment_Detail_Plan_Today;
import com.example.mytodolist.TodayPlan.ItemClickListener;
import com.example.mytodolist.TodayPlan.ToDoListAdapter;
import com.example.mytodolist.db.Db;
import com.example.mytodolist.entity.ToDoList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentHistory extends Fragment implements ItemClickListener {
    TextView tvHis, tvMaximHistory;
    RecyclerView recyclerViewHistory;
    List<ToDoList> listTodo = new ArrayList<>();
    ArrayList<ToDoList> listFinal = new ArrayList<>();
    ToDoListAdapter toDoListAdapter;
    LinearLayoutManager layoutManager;
    Context context;

    public FragmentHistory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listTodo = Db.get().toDoListDAO().getAll();

        String current = getCurrenDateTime();
        for (ToDoList t : listTodo) {
            if (soSanhDate(current, t.ngay) == 1) {
                int a = soSanhDate(current, t.ngay);
                listFinal.add(t);
                System.out.println("kiemtra" + a);
            }
        }

        toDoListAdapter = new ToDoListAdapter(listFinal, context,"history");
        toDoListAdapter.setItemClickListener(this);
        layoutManager = new LinearLayoutManager(context);
    }


    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        tvHis = view.findViewById(R.id.tvHistory);
        tvMaximHistory = view.findViewById(R.id.tvMaximHistory);
        recyclerViewHistory = view.findViewById(R.id.rcvListHistory);


        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewHistory.setAdapter(toDoListAdapter);
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        ToDoList toDoList = listFinal.get(position);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new Fragment_Detail_Plan_Today(toDoList, context, "history"));
        fragmentTransaction.commit();
    }

    @Override
    public void onSwitchClick(View view, int position, boolean checked) {

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

    public String getCurrenDateTime() {
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        String dateTime = formatDate(ngay, thang + 1, nam);

        return dateTime;
    }
}
