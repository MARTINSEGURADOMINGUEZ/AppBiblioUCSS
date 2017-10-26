package com.example.ceups.appprueba;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScrenn extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screnn);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Start the next activity

                Intent intent = new Intent(SplashScrenn.this,LoginActivity.class);
                startActivity(intent);

                finish();
            }
        };

        // Simulate a long loading process on application startup.
            Timer timer = new Timer();

            timer.schedule(task, SPLASH_SCREEN_DELAY);

           /* if(checkConnection(getApplicationContext())==true)
            {
                Redirigir();
            }*/

    }

    public void Redirigir()
    {
        Intent intent = new Intent(SplashScrenn.this,LoginActivity.class);
        startActivity(intent);
    }


    public boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            //Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }

}
