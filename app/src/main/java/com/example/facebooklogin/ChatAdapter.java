package com.example.facebooklogin;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<com.example.facebooklogin.ChatAdapter.CustomViewHolder> {

    List<Response> responseMessages;
    Context context;
    class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public CustomViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textMessage);
        }
    }

    public ChatAdapter(List<Response> responseMessages, Context context) {
        this.responseMessages = responseMessages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(responseMessages.get(position).isMe()){
            return R.layout.me_bubble;
        }if(responseMessages.get(position).isBot()){
            return R.layout.botremind;
        }
        return R.layout.bot_bubble;
    }

    @Override
    public int getItemCount() {
        return  responseMessages.size();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.textView.setText(responseMessages.get(position).getText());
    }
}
