package com.example.tasos.assignmentand;


import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class DATA_SERVICE extends Service{
    private static final String TAG_ID = "id";
    private static final String TAG_USERID = "userid";
    private static final String TAG_long = "longitude";
    private static final String TAG_lat = "latitude";
    private static final String TAG_date = "dt";
    String JSON_STRING;

    ArrayList<Integer> ids = new ArrayList<>();

    @Override
    public void onCreate() {

        new JSONTask().execute("http://62.217.127.19:8000/location");

    }

    public class JSONTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine())!= null){
                    buffer.append(line);
                }

               // return  buffer.toString();
                String finalJson;
                finalJson = buffer.toString();


                JSONArray parentArray = new JSONArray(finalJson);



                int arrSize = parentArray.length();
                Integer id = 0 ;
                String userid = null,dt = null;
                Double lat =0.0 ,lon =0.0;


                    for (int i = 0; i < arrSize; ++i) {
                        JSONObject t = parentArray.getJSONObject(i);
                        //o = a.getJSONObject(i);

                        id = t.getInt("id");
                        ids.add(id);
                        userid = t.getString("userid");
                        lon = t.getDouble("longitude");
                        lat = t.getDouble("latitude");
                        dt = t.getString("dt");

                        Uri uri = Uri.parse("content://com.example.tasos.appprovider/users");
                        ContentValues values = new ContentValues();
                        values.put("_ID", 15);
                        values.put("_USERID", "fdsa");
                        values.put("_LONGITUDE", 58.5);
                        values.put("_LATITUDE", 95);
                        values.put("_DT","faa");

                       // System.out.println("Done!"+i);

                    }
                Log.d("fghj", id.toString() );


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return  null;

        }

        public ArrayList<Integer> getid(){
            return ids;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("json api","DoCreateProduct.doInBackground Json return: "+result);
            //tvData.setText(result);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}