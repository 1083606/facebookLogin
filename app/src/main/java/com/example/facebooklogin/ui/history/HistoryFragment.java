package com.example.facebooklogin.ui.history;

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

public class HistoryFragment extends Fragment {

    View v;
    private RecyclerView myrecyclerview;
    private List<history> lstHistory;

    public HistoryFragment(){

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_history,container,false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.history_recyclerview);
        RecyclerViewAdapter_history recyclerViewAdapter_history = new RecyclerViewAdapter_history(getContext(), lstHistory);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter_history);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstHistory = new ArrayList<>();

        lstHistory.add(new history("早起，養成規律作息","5","50","30"));
        lstHistory.add(new history("不吃宵夜","5","50","30"));
        lstHistory.add(new history("每週運動3次","5","50","30"));
        //lstHistory.add(new history("LULU",5,50,30));

    }
}
