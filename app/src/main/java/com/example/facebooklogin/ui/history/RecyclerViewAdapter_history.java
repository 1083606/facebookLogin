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
        holder.txt_habitName.setText(mData.get(position).getHabbit_name());
        holder.txt_habbit_status.setText(mData.get(position).getHabbit_status());
        holder.txt_completion.setText(mData.get(position).getCompletion());

        holder.txt_original_intention.setText(mData.get(position).getOriginal_intention());
        holder.txt_goodness.setText(mData.get(position).getGoodness());
        holder.txt_badness.setText(mData.get(position).getBadness());
        //holder.txt_post.setText(mData.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_habitName;
        private TextView txt_habbit_status;
        private TextView txt_completion;

        private TextView txt_original_intention;
        private TextView txt_goodness;
        private TextView txt_badness;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_habitName = (TextView) itemView.findViewById(R.id.txt_habitName);
            txt_habbit_status = (TextView) itemView.findViewById(R.id.txt_habbit_status);
            txt_completion = (TextView) itemView.findViewById(R.id.txt_completion);
            txt_original_intention = (TextView) itemView.findViewById(R.id.txt_original_intention);
            txt_goodness = (TextView) itemView.findViewById(R.id.txt_goodness);
            txt_badness = (TextView) itemView.findViewById(R.id.txt_badness);

        }
    }
}
