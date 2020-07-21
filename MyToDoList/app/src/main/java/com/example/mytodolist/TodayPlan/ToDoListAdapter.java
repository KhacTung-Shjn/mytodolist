package com.example.mytodolist.TodayPlan;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.R;
import com.example.mytodolist.entity.ToDoList;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    String kiemtra;
    List<ToDoList> listTodo;
    Context context;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ToDoListAdapter(List<ToDoList> listTodo, Context context, String kiemtra) {
        this.listTodo = listTodo;
        this.context = context;
        this.kiemtra = kiemtra;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_listview_today, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {
        ToDoList toDoList = listTodo.get(position);

        vh.tvTime.setText(toDoList.gio);
        vh.tvContent.setText(toDoList.content);
        if (toDoList.alarm.equalsIgnoreCase("yes")) {
            vh.swAlarm.setChecked(true);
        } else {
            vh.swAlarm.setChecked(false);
        }

        switch (kiemtra) {
            case "history": {
                vh.swAlarm.setChecked(false);
                vh.swAlarm.setEnabled(false);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return listTodo.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlarm;
        TextView tvTime, tvContent;
        Switch swAlarm;
        RelativeLayout backroundItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAlarm = itemView.findViewById(R.id.ivAlarm);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContent = itemView.findViewById(R.id.tvContent);
            swAlarm = itemView.findViewById(R.id.swAlarm);
            backroundItem = itemView.findViewById(R.id.backgroundItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

            swAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    itemClickListener.onSwitchClick(buttonView, getAdapterPosition(), isChecked);
                }
            });
        }

    }

}
