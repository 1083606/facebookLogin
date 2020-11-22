package com.example.facebooklogin.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebooklogin.R;

import java.util.List;

public class RecyclerViewAdapter_cr extends RecyclerView.Adapter<RecyclerViewAdapter_cr.MyViewHolder> {

    private Context mContext;
    private List<cr> mData;

    public RecyclerViewAdapter_cr(Context mContext, List<cr> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    //--------------
    private  OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onLikeClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    //--------------
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_cr,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v,mListener);
        return vHolder;
         /*
        View v;
        LayoutInflater inflater= LayoutInflater.from(mContext);
        v=inflater.inflate(R.layout.item_post,parent,false);
        return  new MyViewHolder(v);
        */
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.role_name.setText(mData.get(position).getRole_name());
        holder.habbit_name.setText(mData.get(position).getHabbit_name());
        holder.updated_at.setText(mData.get(position).getUpdated_at());
        //holder.title.setText(mData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView role_name;
        TextView habbit_name;
        TextView updated_at;
        //ImageView likeButton;

        public MyViewHolder(@NonNull View itemView , OnItemClickListener listener) {
            super(itemView);

            role_name=itemView.findViewById(R.id.txt_role_name);
            habbit_name=itemView.findViewById(R.id.txt_habbit_name);
            updated_at=itemView.findViewById(R.id.txt_updated_at);
            //title=itemView.findViewById(R.id.txtTitle);


            //likeButton=itemView.findViewById(R.id.likeButton);

            //likeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            /*
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onLikeClick(position);
                            likeButton.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                        }
                    }
                }
            });

             */

        }

    }
}
