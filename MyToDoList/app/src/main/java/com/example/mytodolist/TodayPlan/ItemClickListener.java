package com.example.mytodolist.TodayPlan;

import android.view.View;


public interface ItemClickListener {
    void onItemClick(View view, int position);

    void onSwitchClick(View view, int position, boolean checked);
}
