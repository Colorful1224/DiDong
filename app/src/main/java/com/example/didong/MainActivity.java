package com.example.didong;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.didong.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        Button jiaoshui = (Button) findViewById(R.id.jiaoshui);
        Button shouhuo = (Button) findViewById(R.id.shouhuo);
        Button xiaoxi = (Button) findViewById(R.id.xiaoxi);
        EditText fmMessage = (EditText) findViewById(R.id.fmMessage);
        EditText hostNameEdit = (EditText) findViewById(R.id.hostname);
        SharedPreferences settings = getSharedPreferences("settings", 0);
        String hostName = settings.getString("hostName", "");
        hostNameEdit.setText(hostName);
        HttpUtils httpUtil = new HttpUtils();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        JSONObject response = httpUtil.requests("POST", "/test", null);
                        try {
                            Snackbar.make(view, response.get("body").toString(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        //浇水按钮
        jiaoshui.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject datas = new JSONObject("{\"caozuo\":2,\"hostname\":\"" + hostNameEdit.getText() + "\",\"message\":\"\"}");
                            Log.i("data", datas.toString());
                            httpUtil.requests("POST", "/farm/androidAPI", datas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(v, "json Error", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }.start();
            }
        });

        //收获按钮
        shouhuo.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject datas = new JSONObject("{\"caozuo\":1,\"hostname\":\"" + hostNameEdit.getText() + "\",\"message\":\"\"}");

                            Log.i("data", datas.toString());
                            httpUtil.requests("POST", "/farm/androidAPI", datas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(v, "json Error", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }.start();
            }
        });

        //消息按钮
        xiaoxi.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject datas = new JSONObject("{\"caozuo\":3,\"hostname\":\"" + hostNameEdit.getText() + "\",\"message\":\""+fmMessage.getText()+"\"}");

                            Log.i("data", datas.toString());
                            httpUtil.requests("POST", "/farm/androidAPI", datas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(v, "json Error", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }.start();
            }
        });

        hostNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Use 0 or MODE_PRIVATE for the default operation
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("hostName", s.toString());
                editor.commit();

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}