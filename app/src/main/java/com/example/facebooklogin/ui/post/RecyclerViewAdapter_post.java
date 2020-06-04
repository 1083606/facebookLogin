package com.example.facebooklogin.ui.post;

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

public class RecyclerViewAdapter_post extends RecyclerView.Adapter<RecyclerViewAdapter_post.MyViewHolder> {

    Context mContext;
    List<post> mData;

    public RecyclerViewAdapter_post(Context mContext, List<post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_post,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.img_post.setImageResource(mData.get(position).getImage());
        holder.txt_userName.setText(mData.get(position).getName());
        holder.txt_content.setText(mData.get(position).getContent());
        //holder.txt_post.setText(mData.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_post;
        private TextView txt_userName;
        private TextView txt_content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_post = (ImageView) itemView.findViewById(R.id.img_post);
            txt_userName = (TextView) itemView.findViewById(R.id.txt_userName);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);
        }
    }
}
