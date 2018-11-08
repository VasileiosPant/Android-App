package com.example.tasos.assignmentand;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tasos on 28/1/2017.
 */

@SuppressWarnings("deprecation")
public class GPS_SERVICE extends Service {
    private LocationListener listener;
    private LocationManager locationManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        listener = new LocationListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onLocationChanged(Location location) {

                Intent i = new Intent("location_update");
                i.putExtra("coordinates",location.getLongitude()+" "+location.getLatitude());
                sendBroadcast(i);


                String name = "it21402";

                final JSONObject abc = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                DateFormat dt = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss");

                //Date date = new Date();
                try {

                    abc.put("userid", name);
                    abc.put("longitude" , location.getLongitude());
                    abc.put("latitude" , location.getLatitude());
                    abc.put("dt" , dt.format(new Date()));

                    jsonArray.put(abc);

                    Log.d("Json:", "json:" +jsonArray.toString());


                    String jsonData = jsonArray.toString();

                    Thread t = new Thread() {

                        public void run() {
                            Looper.prepare(); //For Preparing Message Pool for the child Thread
                            HttpClient client = new DefaultHttpClient();
                            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                            HttpResponse response;
                            //JSONObject json = new JSONObject();

                            try {
                                HttpPost post = new HttpPost("http://62.217.127.19:8000/location");
                                //json.put("email", email);
                                //json.put("password", pwd);
                                StringEntity se = new StringEntity( abc.toString());
                                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                                post.setEntity(se);
                                response = client.execute(post);

                    /*Checking response */
                                if(response!=null){
                                    InputStream in = response.getEntity().getContent(); //Get the data in the entity
                                }

                            } catch(Exception e) {
                                e.printStackTrace();
                                Log.d("Error", "Cannot Estabilish Connection");
                            }

                            Looper.loop(); //Loop in the message queue
                        }
                    };

                   t.start();







                    //  new DoCreateProduct().execute(jsonData);

                } catch (JSONException e) {
                    e.printStackTrace();
                    // } catch (UnsupportedEncodingException e) {
                    //    e.printStackTrace();
                    // } catch (ClientProtocolException e) {
                    //      e.printStackTrace();
                    //} catch (IOException e) {
                    //  e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                //SystemClock.sleep(5000);
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER) )


        //noinspection MissingPermission

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);
    }




    /*class DoCreateProduct extends AsyncTask<String, Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            String jsonData = params[0];

            try {
                URL url = new URL("http://62.217.127.19:8000/location");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();


                //send data
                OutputStream dos =  httpURLConnection.getOutputStream();
                dos.write(jsonData.getBytes());

                //receive + read data response
                InputStream is = httpURLConnection.getInputStream();
                String result = "";
                int byteCharacter;
                while((byteCharacter = is.read()) != -1){
                    result +=(char)byteCharacter;
                }
                Log.d("json api","DoCreateProduct.doInBackground Json return: "+result);


                is.close();
                dos.close();
                httpURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}