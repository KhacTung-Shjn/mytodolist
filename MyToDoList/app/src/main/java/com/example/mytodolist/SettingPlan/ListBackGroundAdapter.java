package com.example.mytodolist.SettingPlan;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolist.R;
import com.example.mytodolist.TodayPlan.ItemClickListener;


public class ListBackGroundAdapter extends RecyclerView.Adapter<ListBackGroundAdapter.ViewHolder> {
    int[] listImage;
    Context context;

    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ListBackGroundAdapter(int[] listImage, Context context) {
        this.listImage = listImage;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_select_background, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivBackground.setImageResource(listImage[position]);
    }

    @Override
    public int getItemCount() {
        return listImage.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBackground = itemView.findViewById(R.id.ivBackground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
