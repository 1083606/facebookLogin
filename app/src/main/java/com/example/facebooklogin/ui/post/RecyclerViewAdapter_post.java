package com.example.facebooklogin.ui.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebooklogin.R;

import java.util.List;

public class RecyclerViewAdapter_post extends RecyclerView.Adapter<RecyclerViewAdapter_post.MyViewHolder> {

    private Context mContext;
    private List<post> mData;

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
         /*
        View v;
        LayoutInflater inflater= LayoutInflater.from(mContext);
        v=inflater.inflate(R.layout.item_post,parent,false);
        return  new MyViewHolder(v);
        */
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*
        holder.img_user.setImageResource(mData.get(position).getUserImage());
        holder.img_post.setImageResource(mData.get(position).getImage());
        holder.txt_userName.setText(mData.get(position).getName());
        holder.txt_content.setText(mData.get(position).getContent());
        //holder.txt_post.setText(mData.get(position).getName());
         */
        holder.habbit_cat_name.setText(mData.get(position).getHabbit_cat_name());
        holder.user_name.setText(mData.get(position).getUser_name());
        //holder.title.setText(mData.get(position).getTitle());
        holder.content.setText(mData.get(position).getContent());
        holder.updated_at.setText(mData.get(position).getUpdated_at());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        /*
        private ImageView img_user;
        private ImageView img_post;
        private TextView txt_userName;
        private TextView txt_content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user = (ImageView) itemView.findViewById(R.id.userImg);
            img_post = (ImageView) itemView.findViewById(R.id.img_post);
            txt_userName = (TextView) itemView.findViewById(R.id.txt_userName);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);
        }

         */
        TextView habbit_cat_name;
        TextView user_name;
        //TextView title;
        TextView content;
        TextView updated_at;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name=itemView.findViewById(R.id.txtUserData);
            habbit_cat_name=itemView.findViewById(R.id.txtHabbit_cat_name);
            //title=itemView.findViewById(R.id.txtTitle);
            content=itemView.findViewById(R.id.txtContent);
            updated_at=itemView.findViewById(R.id.txtUpdated_at);
        }

    }
}
