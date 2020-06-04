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

//        postViewModel =
//                ViewModelProviders.of(this).get(HistoryViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_post, container, false);
//        final TextView textView = root.findViewById(R.id.text_post);
//        postViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstPost = new ArrayList<>();
        lstPost.add(new post("LULU","堅持50天!!!",R.drawable.first));
        lstPost.add(new post("止靈","讀書達成",R.drawable.book));
        lstPost.add(new post("小洋","多喝水!!!",R.drawable.water));
        lstPost.add(new post("阿明","吃健康的一餐~滿足!!!",R.drawable.food));
        lstPost.add(new post("亦僅","多運動!!!",R.drawable.running));
        lstPost.add(new post("攔鴿","讀書!!!",R.drawable.study));
    }
}
