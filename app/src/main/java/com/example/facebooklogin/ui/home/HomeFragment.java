package com.example.facebooklogin.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.facebooklogin.ChatActivity;
import com.example.facebooklogin.HabitNameActivity;
import com.example.facebooklogin.MainActivity;
import com.example.facebooklogin.NotificationReceiver;
import com.example.facebooklogin.R;
import com.example.facebooklogin.login;
import com.example.facebooklogin.ui.post.PostFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

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
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {
    //cr
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    String user_id;
    String message;
    String text_homeDispaly;
    String click_crId;

    private RecyclerView myrecyclerview;
    private List<cr> lstCr;
    View v;

    //下拉更新---------
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //---------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstCr = new ArrayList<>();
        user_id=readUserID();
        //new AsyncPostReturnUserChatroomBool().execute(user_id);
        //Toast.makeText(getActivity(),user_id,Toast.LENGTH_SHORT).show();
        new AsyncPostselectChatroomID().execute(user_id);

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        user_id=readUserID();
        new AsyncPostReturnUserChatroomBool().execute(user_id);

        //------------------------------------
        v = inflater.inflate(R.layout.fragment_cr, container, false);
        //Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

        //新增聊天室---------------------
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivity(new Intent(getContext(), HabitNameActivity.class));
            }
        });
        myrecyclerview = (RecyclerView) v.findViewById(R.id.cr_recyclerview);
        RecyclerViewAdapter_cr recyclerViewAdapter_cr = new RecyclerViewAdapter_cr(getContext(),lstCr);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter_cr);



        //顯示無聊天室---------------------
        final TextView text_home = v.findViewById(R.id.text_home);
        final ImageView imageViewEmptybox=v.findViewById(R.id.imageViewEmptybox);
        //Toast.makeText(getActivity(),text_homeDispaly,Toast.LENGTH_SHORT).show();
        //---------------------------------------------------------
        //把回應寫入text_homeDispaly
        try {
            String response=new HomeFragment.AsyncPostReturnUserChatroomBool().execute(user_id).get();
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                if (message.equals("Yes")){
                    //text_homeDispaly="";
                    text_home.setText("");
                    imageViewEmptybox.setImageResource(android.R.color.transparent);
                    //Toast.makeText(getActivity(),"有資料",Toast.LENGTH_SHORT).show();
                }else if(message.equals("No")){
                    //Toast.makeText(getActivity(),"尚未建立聊天室，快新增一個吧！",Toast.LENGTH_SHORT).show();
                    //text_homeDispaly="尚未建立聊天室，快新增一個吧！";
                    text_home.setText("尚未建立聊天室，快新增一個吧！");
                    imageViewEmptybox.setImageResource(R.drawable.emptybox);
                    //Toast.makeText(getActivity(),"沒資料",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"message:"+message,Toast.LENGTH_SHORT).show();
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //-------------------------------------
        //-------------------------------------
        return v;
    }


    public class AsyncPostselectChatroomID   extends AsyncTask<String,Void,String>
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
                url = new URL("http://140.131.114.140/chatbot109204/data/selectChatroomID.php");
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
                        .appendQueryParameter("user_id", user_id);
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
            //textView2.setText(strUTF8);
            //Toast.makeText(getActivity(),strUTF8,Toast.LENGTH_LONG).show();

            try {
                JSONObject jsonObject = new JSONObject(strUTF8);
                //Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                JSONArray jsonArray = jsonObject.getJSONArray("records");
                for (int i=0;i<jsonArray.length();i++){
                    //for (int i=jsonArray.length()-1;i>0;i--){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    cr model= new cr();
                    model.setChatroom_id(jsonObject1.getString("chatroom_id"));
                    model.setUser_id(jsonObject1.getString("user_id"));
                    model.setNick_name(jsonObject1.getString("nick_name"));
                    model.setRole_id(jsonObject1.getString("role_id"));
                    model.setRole_name(jsonObject1.getString("role_name"));
                    model.setRole_photo(jsonObject1.getString("role_photo"));
                    model.setHabbit_id(jsonObject1.getString("habbit_id"));
                    model.setHabbit_name(jsonObject1.getString("habbit_name"));
                    model.setHabbit_status(jsonObject1.getString("habbit_status"));
                    model.setSigned_time(jsonObject1.getString("signed_time"));
                    model.setCompletion(jsonObject1.getString("completion"));
                    model.setOriginal_intention(jsonObject1.getString("original_intention"));
                    model.setGoodness(jsonObject1.getString("goodness"));
                    model.setBadness(jsonObject1.getString("badness"));
                    model.setDays(jsonObject1.getString("days"));
                    model.setCreated_at(jsonObject1.getString("created_at"));
                    model.setUpdated_at(jsonObject1.getString("updated_at"));
                    lstCr.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PutDataIntoRecyclerView(lstCr);
        }
    }

    private void PutDataIntoRecyclerView(List<cr> crList){
        //v = inflater.inflate(R.layout.fragment_post,container,false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.cr_recyclerview);
        RecyclerViewAdapter_cr recyclerViewAdapter_cr = new RecyclerViewAdapter_cr(getContext(),lstCr);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter_cr);

        recyclerViewAdapter_cr.setOnItemClickListener(new RecyclerViewAdapter_cr.OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
                click_crId=lstCr.get(position).getChatroom_id();
                //Toast.makeText(getActivity(),click_crId,Toast.LENGTH_SHORT).show();
                //--到聊天室
                startActivity(new Intent(getContext(), ChatActivity.class));
            }

            //-------------------
            //like觸發-----------
            //-------------------
            @Override
            public void onLikeClick(int position) {

            }
        });
    }





    public class AsyncPostReturnUserChatroomBool   extends AsyncTask<String,Void,String>
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
                url = new URL("http://140.131.114.140/chatbot109204/data/returnUserChatroomBool.php");
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
                        .appendQueryParameter("user_id", user_id);
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
            try{
                JSONObject jsonObject = new JSONObject(strUTF8);
                /*
                message = jsonObject.getString("message");
                if (message.equals("Yes")){
                    text_homeDispaly="";
                    //Toast.makeText(getActivity(),"有資料",Toast.LENGTH_SHORT).show();
                }else if(message.equals("No")){
                    Toast.makeText(getActivity(),"尚未建立聊天室，快新增一個吧！",Toast.LENGTH_SHORT).show();
                    //text_homeDispaly="尚未建立聊天室，快新增一個吧！";
                    //Toast.makeText(getActivity(),"沒資料",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"message:"+message,Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                 */
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private String readUserID(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        return preferences.getString("UserID","無");
    }
}

