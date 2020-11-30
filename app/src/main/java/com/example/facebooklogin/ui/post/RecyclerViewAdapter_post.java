package com.example.facebooklogin.ui.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebooklogin.R;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;

import java.util.List;

public class RecyclerViewAdapter_post extends RecyclerView.Adapter<RecyclerViewAdapter_post.MyViewHolder> {

    private Context mContext;
    private List<post> mData;

    public RecyclerViewAdapter_post(Context mContext, List<post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    //--------------
    private  OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onThumb_onClick(int position);
        //void onViewClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    //--------------
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_post,parent,false);
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
        holder.habbit_cat_name.setText(mData.get(position).getHabbit_cat_name());
        holder.user_name.setText(mData.get(position).getUser_name());
        //holder.title.setText(mData.get(position).getTitle());
        holder.content.setText(mData.get(position).getContent());
        holder.updated_at.setText(mData.get(position).getUpdated_at());
        //holder.likeButton.setImageResource(false);
        //holder.likesNum.setText(mData.get(position).getLikesNum());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView habbit_cat_name;
        TextView user_name;
        //TextView title;
        TextView content;
        TextView updated_at;
        //TextView likesNum;
        ImageView likeButton;
        RelativeLayout rl;
        LikeButton thumb_on;

        public MyViewHolder(@NonNull View itemView , OnItemClickListener listener) {
            super(itemView);

            user_name=itemView.findViewById(R.id.txtUserData);
            habbit_cat_name=itemView.findViewById(R.id.txtHabbit_cat_name);
            //title=itemView.findViewById(R.id.txtTitle);
            content=itemView.findViewById(R.id.txtContent);
            updated_at=itemView.findViewById(R.id.txtUpdated_at);
            //likesNum=itemView.findViewById(R.id.txtLikesNum);

            //likeButton=itemView.findViewById(R.id.likeButton);
            //likeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            thumb_on=itemView.findViewById(R.id.thumb_on);
            //rl=itemView.findViewById(R.id.rl);


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
                            //likeButton.setAnimationScaleFactor(2);
                            likeButton.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                        }
                    }
                }
            });

             */
            /*
            rl.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onViewClick(position);
                            //likeButton.setAnimationScaleFactor(2);
                        }
                    }
                }
            });

             */
            thumb_on.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onThumb_onClick(position);
                            //likeButton.setAnimationScaleFactor(2);
                            thumb_on.setLiked(true);
                        }
                    }
                }
            });



        }

    }



}
