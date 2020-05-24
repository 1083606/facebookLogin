package com.example.facebooklogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class login extends AppCompatActivity {
    private LoginButton loginButton;
    /*
    private CircleImageView circleImageView;
    private TextView txtName,txtEmail,txtInfo;
    */
    private CallbackManager callbackManager;
    private java.lang.Object Object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        /*
        txtName = findViewById(R.id.profile_name);
        txtEmail = findViewById(R.id.profile_email);
        txtInfo = findViewById(R.id.profile_info);

        circleImageView = findViewById(R.id.profile_pic);
        */
        callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions(Arrays.asList("public_profile","email"));
        //--new----------------------------
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("response",response.toString());
                        getData(object);

                        Toast.makeText(login.this,"登入成功",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setClass(login.this ,GetUserName.class );
                        startActivity(intent);
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
            Toast.makeText(login.this,userId+"登入成功",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClass(login.this ,MainActivity.class );
            startActivity(intent);
            //txtInfo.setText(AccessToken.getCurrentAccessToken().getUserId());
        }
        //---newdofnew-----------------------------
    }

    //---------------------------------------------------------------------------
    private void getData(JSONObject object) {
        try {
            URL profile_picture = new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");
            /*
            Glide.with(login.this).load(profile_picture.toString()).into(circleImageView);

            txtEmail.setText(object.getString("email"));
            txtName.setText(object.getString("name"));
            */
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
}
