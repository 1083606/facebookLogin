package com.example.facebooklogin.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.facebooklogin.HabitNameActivity;
import com.example.facebooklogin.R;

public class HomeFragment extends Fragment {
    /*
    //-------------------------
    Button b1;
    //-------------------------
    */
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //------------------------------
        Button b1=view.findViewById(R.id.create_room);

        b1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HabitNameActivity.class));
            }
        });

        //------------------------------
        final TextView textView = view.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        /*------------------------------
        b1=v.findViewById(R.id.create_room);
        b1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                fragment_habit_name fragment_habit_name = new fragment_habit_name ();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.layout.fragment_home,fragment_habit_name);
                transaction.commit();
            }
        });
        //------------------------------
        */
        return view;
    }


}
