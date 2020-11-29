package com.example.facebooklogin.ui.post;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.example.facebooklogin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
//import recyclerViewAdapter_post.OnItemClickListener;

public class PostFragment extends Fragment {
    //cr
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    String user_id,click_postId;
    //下拉更新---------
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //---------------
    View v;
    private RecyclerView myrecyclerview;
    private List<post> lstPost;
    //-------------------------------
    ImageView likeButton;
    //---------------
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


    public View onCreateView (@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_post,container,false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.cr_recyclerview);
        RecyclerViewAdapter_post recyclerViewAdapter_post = new RecyclerViewAdapter_post(getContext(),lstPost);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter_post);

        //--------------------------------------

        //--------------------------------------

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
        String [] values ={"全部","自己","按讚的","讀書","喝水","運動","早睡"};
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

                for (int i=0;i<jsonArray.length();i++){
                //for (int i=jsonArray.length()-1;i>0;i--){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    post model= new post();
                    model.setPost_id(jsonObject1.getString("post_id"));
                    model.setHabbit_cat_name(jsonObject1.getString("habbit_cat_name"));
                    model.setUser_id(jsonObject1.getString("user_id"));
                    model.setUser_name(jsonObject1.getString("user_name"));
                    model.setContent(jsonObject1.getString("content"));
                    model.setCreated_at(jsonObject1.getString("created_at"));
                    model.setUpdated_at(jsonObject1.getString("updated_at"));
                    model.setLikesNum(jsonObject1.getString("likesNum"));
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
        myrecyclerview = (RecyclerView) v.findViewById(R.id.cr_recyclerview);
        RecyclerViewAdapter_post recyclerViewAdapter_post = new RecyclerViewAdapter_post(getContext(),lstPost);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter_post);

        recyclerViewAdapter_post.setOnItemClickListener(new RecyclerViewAdapter_post.OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
            }

            //-------------------
            //like觸發-----------
            //-------------------
            @Override
            public void onLikeClick(int position) {
                click_postId=lstPost.get(position).getPost_id();

                //Toast.makeText(getActivity(),readUserID()+"like!"+click_postId,Toast.LENGTH_SHORT).show();
                user_id=readUserID();
                //Toast.makeText(getActivity(),SteingOf(postlike_result,Toast.LENGTH_SHORT).show();
                new AsyncLikePost().execute(user_id,click_postId);


            }

            @Override
            public void onThumb_onClick(int position) {
                click_postId=lstPost.get(position).getPost_id();

                Toast.makeText(getActivity(),readUserID()+"like!"+click_postId,Toast.LENGTH_SHORT).show();
                user_id=readUserID();
                //Toast.makeText(getActivity(),SteingOf(postlike_result,Toast.LENGTH_SHORT).show();
                new AsyncLikePost().execute(user_id,click_postId);
            }

        });
    }

    //-------------------------------------
    //cr 新增讚
    //-------------------------------------
    private class AsyncLikePost   extends AsyncTask<String,Void,String>
    {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://140.131.114.140/chatbot109204/data/likePost.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", params[0])
                        .appendQueryParameter("post_id", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return(String.valueOf(result));
                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String strUTF8) {
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(strUTF8);
                String result = jsonObject.getString("result");
                int resultValue=Integer.parseInt(result);
                if (resultValue==0){
                    //cr 成功，取出 data
                    String data = jsonObject.getString("data");
                    Toast.makeText(getActivity(),result+data,Toast.LENGTH_SHORT).show();
//                    lstPost.clear();
//                    GetData getData = new GetData();
//                    getData.execute();

                }else{
                    //代表user要收回讚--dislikePost
                    String data = jsonObject.getString("data");
                    String error = jsonObject.getString("error");
                    Toast.makeText(getActivity(), result+data+error, Toast.LENGTH_SHORT).show();
                    new AsyncDislikePost().execute(user_id,click_postId);
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //-------------------------------------
    //cr 取消讚
    //-------------------------------------
    private class AsyncDislikePost   extends AsyncTask<String,Void,String>
    {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://140.131.114.140/chatbot109204/data/dislikePost.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", params[0])
                        .appendQueryParameter("post_id", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return(String.valueOf(result));
                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String strUTF8) {
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(strUTF8);
                String result = jsonObject.getString("result");
                int resultValue=Integer.parseInt(result);
                if (resultValue==0){
                    //取消讚成功，取出 data
                    String data = jsonObject.getString("data");
                    Toast.makeText(getActivity(),result+data,Toast.LENGTH_SHORT).show();
                    //重新載data----------------------
//                    lstPost.clear();
//                    GetData getData = new GetData();
//                    getData.execute();
                }else{
                    String data = jsonObject.getString("data");
                    String error = jsonObject.getString("error");
                    Toast.makeText(getActivity(), result+data+error, Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private String readUserID(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        return preferences.getString("UserID","未存任何資料");
    }
}
