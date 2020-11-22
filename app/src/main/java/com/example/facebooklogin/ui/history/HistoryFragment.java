package com.example.facebooklogin.ui.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class HistoryFragment extends Fragment {
    String userId,userName;
    //cr
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;



    View v;
    private RecyclerView myrecyclerview;
    private List<history> lstHistory;
    private static String Json_URL = "http://140.131.114.140/chatbot109204/data/getUserHistory.php";

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
        userId=readUserID();
        GetData getData = new GetData();
        getData.execute(userId);
        /*
        lstHistory.add(new history("早起，養成規律作息","5","50","30"));
        lstHistory.add(new history("不吃宵夜","5","50","30"));
        lstHistory.add(new history("每週運動3次","5","50","30"));
        //lstHistory.add(new history("LULU",5,50,30));

         */

    }

    private String readUserID(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        return preferences.getString("UserID","未存任何資料");
    }

    public class GetData extends AsyncTask<String,String,String> {
        //--------------------
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)

        //--------------------

        @Override
        protected String doInBackground(String... params) {
            /*

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

             */
            try {

                // Enter URL address where your php file resides
                url = new URL("http://140.131.114.140/chatbot109204/data/getUserHistory.php");

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
                        .appendQueryParameter("user_id", params[0]);
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
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("records");

                for (int i=0;i<jsonArray.length();i++){
                    //for (int i=jsonArray.length()-1;i>0;i--){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    history model= new history();
                    model.setChatroom_id(jsonObject1.getString("chatroom_id"));
                    model.setUser_id(jsonObject1.getString("user_id"));
                    model.setHabbit_name(jsonObject1.getString("habbit_name"));
                    model.setHabbit_status(jsonObject1.getString("habbit_status"));
                    model.setCompletion(jsonObject1.getString("completion"));
                    model.setMax_completion(jsonObject1.getString("max_completion"));
                    model.setOriginal_intention(jsonObject1.getString("original_intention"));
                    model.setGoodness(jsonObject1.getString("goodness"));
                    model.setBadness(jsonObject1.getString("badness"));
                    model.setCreated_at(jsonObject1.getString("created_at"));
                    model.setUpdated_at(jsonObject1.getString("updated_at"));
                    lstHistory.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PutDataIntoRecyclerView(lstHistory);
        }
    }

    private void PutDataIntoRecyclerView(List<history> historyList){
        //v = inflater.inflate(R.layout.fragment_post,container,false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.history_recyclerview);
        RecyclerViewAdapter_history recyclerViewAdapter_history = new RecyclerViewAdapter_history(getContext(),lstHistory);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter_history);
    }




}
