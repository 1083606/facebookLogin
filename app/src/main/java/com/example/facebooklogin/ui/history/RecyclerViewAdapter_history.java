package com.example.facebooklogin.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebooklogin.R;

import java.util.List;

public class RecyclerViewAdapter_history extends RecyclerView.Adapter<RecyclerViewAdapter_history.MyViewHolder> {

    Context mContext;
    List<history> mData;

    public RecyclerViewAdapter_history(Context mContext, List<history> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_history,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_habitName.setText(mData.get(position).getHabitName());
        holder.txt_contiDay.setText(mData.get(position).getContiDay());
        holder.txt_accumDay.setText(mData.get(position).getAccumDay());
        holder.txt_maxContiDay.setText(mData.get(position).getMaxContiDay());
        //holder.txt_post.setText(mData.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_habitName;
        private TextView txt_contiDay;
        private TextView txt_accumDay;
        private TextView txt_maxContiDay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_habitName = (TextView) itemView.findViewById(R.id.txt_habitName);
            txt_contiDay = (TextView) itemView.findViewById(R.id.txt_contiDay);
            txt_accumDay = (TextView) itemView.findViewById(R.id.txt_accumDay);
            txt_maxContiDay = (TextView) itemView.findViewById(R.id.txt_maxContiDay);
        }
    }
}
