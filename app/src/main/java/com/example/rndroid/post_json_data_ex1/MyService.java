package com.example.rndroid.post_json_data_ex1;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyService extends Service {

    private String name, conuntry, twitter;
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
         name = bundle.getString("name");
        conuntry = bundle.getString("country");
        twitter = bundle.getString("twitter");
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("http://hmkcode.appspot.com/jsonservlet");
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String>{

        HttpURLConnection urlConnection; OutputStream outputStream; OutputStreamWriter outputStreamWriter;
        @Override
        protected String doInBackground(String... strings) {
            String s = strings[0];
            try {
                URL url = new URL(s);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("content-type","application/json");

                // prepare JSON Data for posting
                JSONObject jsonObject = new JSONObject();// {}
                jsonObject.accumulate("name", name);// {"name": "rishi"}
                jsonObject.accumulate("country", conuntry); //{"country": "india"}
                jsonObject.accumulate("twitter", twitter); // {"twitter": "@rishi"}
                outputStream = urlConnection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);

                // write JSON data into outputstream writer
                outputStreamWriter.write(jsonObject.toString());
                // forcefully throw everything to server
                outputStreamWriter.flush();
                //

                // HERE AT THIS POINT OF TIME - SERVER WILL START READING
                // LET US ASK SERVER FOR - RESPONSE

                int responsecode = urlConnection.getResponseCode();
                // return response to onPost execute
                if (responsecode == HttpURLConnection.HTTP_OK){
                    return "succes";
                }else {
                    return "failure";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                // close all conections properly in finally
                if(urlConnection != null){
                    urlConnection.disconnect();
                    if (outputStream != null){
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        if (outputStreamWriter != null){
                            outputStreamWriter.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MyService.this, ""+s, Toast.LENGTH_SHORT).show();

            // staring brodcast to post data to textview
            Intent intent = new Intent();
            intent.setAction("show_response");
            intent.putExtra("response", s);
            sendBroadcast(intent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
