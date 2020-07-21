package com.example.mytodolist.SettingPlan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.R;
import com.example.mytodolist.TodayPlan.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentBackrground extends Fragment implements ItemClickListener {

    Context context;
    TextView tvBackGround;
    RecyclerView recyclerViewBackground;
    ListBackGroundAdapter listBackGroundAdapter;
    GridLayoutManager gridLayoutManager;
    int[] listImage;
    GuiDuLieu guiDuLieu;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int stt;

    public FragmentBackrground(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listImage = new int[]{R.drawable.hinhnen0, R.drawable.hinhnen1, R.drawable.hinhnen2, R.drawable.hinhnen3,
                R.drawable.hinhnen4, R.drawable.hinhnen5};

        listBackGroundAdapter = new ListBackGroundAdapter(listImage, context);
        gridLayoutManager = new GridLayoutManager(context, 2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list_background, container, false);

        recyclerViewBackground = view.findViewById(R.id.rcvListBackground);
        tvBackGround = view.findViewById(R.id.tvBackground);

        recyclerViewBackground.setLayoutManager(gridLayoutManager);
        recyclerViewBackground.setAdapter(listBackGroundAdapter);
        listBackGroundAdapter.setItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        guiDuLieu.guiDuLieu(position);
    }

    @Override
    public void onSwitchClick(View view, int position, boolean checked) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        guiDuLieu = (GuiDuLieu) context;
    }
}
