package com.example.facebooklogin;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class login extends AppCompatActivity {

    //cr
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    //userId,userName
    String userId,userName;
    //image
    URL profile_picture;


    //動畫animation
    private LoginButton loginButton;
    ImageView applogo,chicken;
    Animation frombottom;
    //----------------------
    //private CircleImageView circleImageView;
    private TextView txtresult,txtdata;
    //------------------------
    private CallbackManager callbackManager;
    private java.lang.Object Object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //動畫animation
        applogo=(ImageView) findViewById(R.id.applogo);
        chicken=(ImageView) findViewById(R.id.chicken);
        loginButton = findViewById(R.id.login_button);

        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);
        applogo.setAnimation(frombottom);
        chicken.setAnimation(frombottom);
        loginButton.setAnimation(frombottom);
        //----------------------
        txtresult = findViewById(R.id.txtresult);
        txtdata = findViewById(R.id.txtdata);
        //txtInfo = findViewById(R.id.profile_info);

        //circleImageView = findViewById(R.id.profile_pic);
        //----------------------
        callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions(Arrays.asList("public_profile","email"));
        //--facebook Login----------------------------
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("response",response.toString());
                        getData(object);
                        Toast.makeText(login.this,"登入成功",Toast.LENGTH_LONG).show();
                    }
                });
                //request Graph API
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,name");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {}
            @Override
            public void onError(FacebookException error) {}
        });
        //---endofNew---------------------------
        printKeyHash();
        //---new-----------------------------
        //If 已經登入
        if (AccessToken.getCurrentAccessToken() != null){
            String userId=AccessToken.getCurrentAccessToken().getUserId();
            //獲取資料庫 userName
            new AsyncPostGetName().execute(userId);
         }
    }

    //--------------------------------------------------------------------------
    //-------------------------------------
    //獲取已登入userName
    //-------------------------------------
    public class AsyncPostGetName   extends AsyncTask<String,Void,String>
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
                url = new URL("http://140.131.114.140/chatbot109204/data/getUserName.php");
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
        protected void onPostExecute(String strUTF8) {
            //mTxtResult.setText(strUTF8);
            try{
                JSONObject jsonObject = new JSONObject(strUTF8);
                String user_id = jsonObject.getString("user_id");
                String user_name = jsonObject.getString("user_name");
                Toast.makeText(login.this, user_id+user_name, Toast.LENGTH_SHORT).show();
                //傳入成功，跳至主頁面
                //Toast.makeText(login.this,user_id+"登入成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(login.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("UserID",user_id);
                bundle.putString("UserName",user_name);
                intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
                startActivity(intent);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //---------------------------------------------------------------------------
    private void getData(JSONObject object) {
        try {
            profile_picture = new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");
            //----------------------
            //Glide.with(login.this).load(profile_picture.toString()).into(circleImageView);

            new AsyncLogin ().execute(object.getString("id"),object.getString("name"),object.getString("email"));
            userId=object.getString("id");
            userName=object.getString("name");
            /*
            txtEmail.setText(object.getString("email"));
            txtName.setText(object.getString("name"));
            txtInfo.setText(object.getString("id"));
            */
            //----------------------
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*
    AccessTokenTracker tokenTracker =new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                txtName.setText("");
                txtEmail.setText("");
                txtInfo.setText("");
                circleImageView.setImageResource(0);
                Toast.makeText(MainActivity.this,"已登出",Toast.LENGTH_LONG).show();
            }
            else
                loadUserProfile(currentAccessToken);
        }
    };
    */

    //----------------------------------------
    private void printKeyHash(){
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo("com.example.facebooklogin", PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures){
                MessageDigest md =MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    //-------------------------------------
    //使用者註冊 register
    //-------------------------------------
    private class AsyncLogin   extends AsyncTask<String,String,String>
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
                url = new URL("http://140.131.114.140/chatbot109204/data/userRegister.php");
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
                        .appendQueryParameter("user_name", params[1])
                        .appendQueryParameter("mail", params[2]);
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
                String data = jsonObject.getString("data");
                txtresult.setText(result);
                txtdata.setText(data);
                checkResultData(Integer.parseInt(result));
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }

        private void checkResultData(int result){
            if (result==0){
                //註冊成功，跳至"使用者資料"
                Intent intent = new Intent();
                intent.setClass(login.this ,GetUserData.class );
                Bundle bundle = new Bundle();
                bundle.putString("UserID",userId);
                bundle.putString("UserName",userName);
                intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦

                startActivity(intent);

            }else{
                //已註冊過->直接到主頁
                Intent intent = new Intent();
                intent.setClass(login.this ,MainActivity.class );
                Bundle bundle = new Bundle();
                bundle.putString("UserID",userId);
                bundle.putString("UserName",userName);
                intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦

                startActivity(intent);
               /*
                //Test
                Intent intent = new Intent();
                intent.setClass(login.this ,GetUserData.class );
                Bundle bundle = new Bundle();
                bundle.putString("UserID",userId);
                bundle.putString("UserName",userName);
                //bundle.putString("profile_picture", String.valueOf(profile_picture));
                intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去哦
                startActivity(intent);

                 */
            }
        }


    }
}
