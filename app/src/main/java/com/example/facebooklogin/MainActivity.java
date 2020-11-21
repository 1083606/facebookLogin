package com.example.facebooklogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


//------------------
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;


public class MainActivity extends AppCompatActivity {
    String UserID,UserName;
    private TextView txtUserName;
    private AppBarConfiguration mAppBarConfiguration;
    //---------fb logout
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //點選fab
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = findViewById(R.id.fab);
               fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this ,PostDetailActivity.class );
                startActivity(intent);
            }
        });

        //----------
        callbackManager = CallbackManager.Factory.create();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_history, R.id.nav_slideshow,R.id.nav_post,R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //-------------------------------------------------
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int menuId = destination.getId();

                switch (menuId){
                    case R.id.nav_home:
                        //Toast.makeText(MainActivity.this,"點選主頁",Toast.LENGTH_LONG).show();
                        fab.hide();
                        break;
                    case R.id.nav_history:
                        fab.hide();
                        break;
                    case R.id.nav_post:
                        fab.show();
                        break;
                    default:
                        fab.hide();
                        break;

                }
            }
        });

        //接收從getUserName的bundle
        Bundle bundle = getIntent().getExtras();
        UserName = bundle.getString("UserName");
        UserID = bundle.getString("UserID");
        //存 userID and userName 至手機本地端
        SharedPreferences sharedPreferences = getSharedPreferences("Userdata" , MODE_PRIVATE);
        sharedPreferences.edit().putString("UserID", UserID).apply();
        sharedPreferences.edit().putString("UserName" , UserName).apply();
        Toast.makeText(MainActivity.this,UserID+UserName,Toast.LENGTH_SHORT).show();

        //View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
       // headerView.findViewById(R.id.txtUserName);

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_userName);
        navUsername.setText(UserName);
        //txtUserName.setText(UserName);
        //-------------------------------------------------

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //----------------
    //點擊action bar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        // menu item click handling
        if (id==R.id.logout){
            new TTFancyGifDialog.Builder(MainActivity.this)
                    .setTitle("確定要登出？")
                    //.setMessage("You don't have time for shopping, Visit our website for online shopping with discount price.")
                    .setPositiveBtnText("確定")
                    .setPositiveBtnBackground("#22b573")
                    .setNegativeBtnText("取消")
                    .setNegativeBtnBackground("#c1272d")
                    .setGifResource(R.drawable.chicken_gif)      //pass your gif, png or jpg
                    .isCancellable(true)
                    .OnPositiveClicked(new TTFancyGifDialogListener() {
                        @Override
                        public void OnClick() {
                            //點選"確認登出"
                            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                                    .Callback() {
                                @Override
                                public void onCompleted(GraphResponse graphResponse) {

                                    SharedPreferences pref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.clear();
                                    editor.commit();
                                    LoginManager.getInstance().logOut();

                                    Intent logoutint = new Intent(MainActivity.this ,login.class);
                                    logoutint.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(logoutint);

                                    Toast.makeText(MainActivity.this,"登出成功",Toast.LENGTH_SHORT).show();

                                }
                            }).executeAsync();
                        }
                    })
                    .OnNegativeClicked(new TTFancyGifDialogListener() {
                        @Override
                        public void OnClick() {
                            Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build();
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
