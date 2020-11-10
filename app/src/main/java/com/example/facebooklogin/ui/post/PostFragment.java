package com.example.facebooklogin.ui.post;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.facebooklogin.HabitMotivationActivity;
import com.example.facebooklogin.HabitNameActivity;
import com.example.facebooklogin.R;
import com.example.facebooklogin.login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {
    //下拉更新---------
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //---------------
    View v;
    private RecyclerView myrecyclerview;
    private List<post> lstPost;
    //-------------------------------
    private static String Json_URL = "http://140.131.114.140/chatbot109204/data/countLikes.php";

    SwipeRefreshLayout swipeRefreshLayout;
    public PostFragment(){

    }
    //-spinner---------
    Spinner spinnerHabbitCat;
    ArrayList<String> habbitCatList = new ArrayList<>();
    ArrayAdapter habbitCatAdapter;
    RequestQueue requestQueue;
    //---------------------


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_post,container,false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.post_recyclerview);
        RecyclerViewAdapter_post recyclerViewAdapter_post = new RecyclerViewAdapter_post(getContext(),lstPost);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter_post);

        //下拉更新--------------
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                recyclerViewAdapter_post.notifyDataSetChanged();
                lstPost.clear();
                GetData getData = new GetData();
                getData.execute();
            }
        });
        //--------------------------------------

        String [] values =
                {"全部","自己","按讚的","讀書","喝水","運動","早睡"};
        Spinner spinner = (Spinner) v.findViewById(R.id.spinnerHabbitCat);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        return v;


    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstPost = new ArrayList<>();
        /*
        lstPost.add(new post(R.drawable.singer,"LULU","堅持50天!!!堅持50天!!!堅持50天!!!fighting",R.drawable.first));
        lstPost.add(new post(R.drawable.singer2,"止靈","讀書達成",R.drawable.book));
        lstPost.add(new post(R.drawable.sing,"小洋","多喝水!!!",R.drawable.water));
        lstPost.add(new post(R.drawable.girl,"阿明","吃健康的一餐~滿足!!!",R.drawable.food));
        lstPost.add(new post(R.drawable.chicken,"亦僅","多運動!!!",R.drawable.running));
        lstPost.add(new post(R.drawable.chatlogo,"攔鴿","讀書!!!",R.drawable.study));
        */
        GetData getData = new GetData();
        getData.execute();
    }


    public class GetData extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection=null;

                try {
                    url= new URL(Json_URL);
                    urlConnection = (HttpURLConnection)url.openConnection();

                    InputStream is=urlConnection.getInputStream();
                    InputStreamReader isr= new InputStreamReader(is);

                    int data = isr.read();
                    while (data != -1){
                        current +=(char)data;
                        data = isr.read();
                    }
                    return  current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (urlConnection!=null){
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("records");

                //for (int i=0;i<jsonArray.length();i++){
                for (int i=jsonArray.length()-1;i>0;i--){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    post model= new post();
                    model.setPost_id(jsonObject1.getString("post_id"));
                    model.setHabbit_cat_name(jsonObject1.getString("habbit_cat_name"));
                    model.setUser_id(jsonObject1.getString("user_id"));
                    model.setUser_name(jsonObject1.getString("user_name"));
                    model.setContent(jsonObject1.getString("content"));
                    model.setCreated_at(jsonObject1.getString("created_at"));
                    model.setUpdated_at(jsonObject1.getString("updated_at"));

                    lstPost.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(lstPost);
        }
    }

    private void PutDataIntoRecyclerView(List<post> postList){
        //v = inflater.inflate(R.layout.fragment_post,container,false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.post_recyclerview);
        RecyclerViewAdapter_post recyclerViewAdapter_post = new RecyclerViewAdapter_post(getContext(),lstPost);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter_post);
    }

}
