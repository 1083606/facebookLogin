package com.example.facebooklogin.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebooklogin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    View v;
    private RecyclerView myrecyclerview;
    private List<post> lstPost;

    public PostFragment(){

    }

//    private HistoryViewModel postViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_post,container,false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.post_recyclerview);
        RecyclerViewAdapter_post recyclerViewAdapter_post = new RecyclerViewAdapter_post(getContext(),lstPost);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter_post);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstPost = new ArrayList<>();
        lstPost.add(new post(R.drawable.singer,"LULU","堅持50天!!!堅持50天!!!堅持50天!!!fighting",R.drawable.first));
        lstPost.add(new post(R.drawable.singer2,"止靈","讀書達成",R.drawable.book));
        lstPost.add(new post(R.drawable.sing,"小洋","多喝水!!!",R.drawable.water));
        lstPost.add(new post(R.drawable.girl,"阿明","吃健康的一餐~滿足!!!",R.drawable.food));
        lstPost.add(new post(R.drawable.chicken,"亦僅","多運動!!!",R.drawable.running));
        lstPost.add(new post(R.drawable.chatlogo,"攔鴿","讀書!!!",R.drawable.study));
    }

}
