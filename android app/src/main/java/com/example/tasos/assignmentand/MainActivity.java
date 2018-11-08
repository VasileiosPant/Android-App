package com.example.tasos.assignmentand;

import android.Manifest;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private Button btn_start, btn_stop,btn3,btn4s;
    private TextView textView;
    private BroadcastReceiver broadcastReceiver;
    private LocationManager locationManager;


    @Override
    protected void onResume() {
        super.onResume();

        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    textView.append("\n" +intent.getExtras().get("coordinates"));
                }
            };
        }

        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = (Button) findViewById(R.id.button);
        btn_stop = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4s = (Button) findViewById(R.id.button4);
        textView = (TextView) findViewById(R.id.textView);
        Uri uri = Uri.parse("content://com.example.tasos.appprovider/users");
        ContentValues values = new ContentValues();
        values.put("_ID", 123);
        values.put("_USERID", "kalispera");
        values.put("_LATITUDE", 92.5);
        values.put("_LONGITUDE", 99);
        values.put("_DT", "aurio");
        Uri resultUri = getContentResolver().insert(uri, values);
        Cursor mCursor = getContentResolver().query(uri, null, null, null, null);
        mCursor.moveToFirst();
        Toast.makeText(getApplicationContext(), mCursor.getString(2), Toast.LENGTH_LONG).show();

        Toast.makeText(getApplicationContext(), "It Works!", Toast.LENGTH_LONG).show();


      /*  Uri uri = Uri.parse("content://com.example.tasos.appprovider/users");
        ContentValues values = new ContentValues();
        values.put("_ID", 123);
        values.put("_USERID", "kalispera");
        values.put("_LATITUDE", 92.5);
        values.put("_LONGITUDE", 99);
        values.put("_DT", "aurio");
        Uri resultUri = getContentResolver().insert(uri, values);
        Cursor mCursor = getContentResolver().query(uri, null, null, null, null);
        mCursor.moveToFirst();*/
        //Uri resultUri = this.getContentResolver().insert(uri, values);
       //Cursor mCursor = this.getContentResolver().query(uri, null, null, null, null);
        //mCursor.moveToFirst();


        //Toast.makeText(getApplicationContext(), mCursor.getString(2), Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(), "It Works!", Toast.LENGTH_LONG).show();

        if (!runtime_permission()) {

            enable_buttons();

            }
        }
    

    private void enable_buttons(){
         btn_start.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent i = new Intent(getApplicationContext(), GPS_SERVICE.class);
             startService(i);
          }
         });


        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),GPS_SERVICE.class);
                stopService(i);


            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent z = new Intent(getApplicationContext(),DATA_SERVICE.class);
                                        startService(z);

                                    }

                                });


        btn4s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(getApplicationContext(),DATA_SERVICE.class);
                stopService(z);
            }

        });
    }

    private boolean runtime_permission(){
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                    .ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
                //SystemClock.sleep(5000);
            } else {

                runtime_permission();

            }
        }
    }

}