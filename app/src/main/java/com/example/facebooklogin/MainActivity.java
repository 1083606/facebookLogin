package com.example.facebooklogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CircleImageView circleImageView;
    private TextView txtName,txtEmail,txtInfo;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        txtName = findViewById(R.id.profile_name);
        txtEmail = findViewById(R.id.profile_email);
        txtInfo = findViewById(R.id.profile_info);

        circleImageView = findViewById(R.id.profile_pic);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions(Arrays.asList("email","public_profile"));
        checkLoginStatus();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                /*
                txtInfo.setText("使用者id： "+loginResult.getAccessToken().getUserId());
                String image_url = "https://graph.facebook.com/"+loginResult.getAccessToken().getUserId()+"/picture?return_ssl_resource=1";
                Glide.with(MainActivity.this).load(image_url).into(circleImageView);
                */


                Toast.makeText(MainActivity.this,"登入成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this ,GetUserName.class );
                startActivity(intent);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

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

    //--試試試試看
    private void loadUserProfile(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            //String first_name = object.getString("first_name");
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";

                            txtEmail.setText(email);
                            txtName.setText(name);
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.dontAnimate();

                            Glide.with(MainActivity.this).load(image_url).into(circleImageView);

                            FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        request.setParameters(parameters);
        request.executeAsync();
        /*
        GraphRequest request =GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    //String first_name = object.getString("first_name");
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";

                    txtEmail.setText(email);
                    txtName.setText(name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with(MainActivity.this).load(image_url).into(circleImageView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        */
    }

    private void checkLoginStatus(){
        if (AccessToken.getCurrentAccessToken() != null){
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }
}
