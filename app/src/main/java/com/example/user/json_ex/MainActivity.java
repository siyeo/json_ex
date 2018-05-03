package com.example.user.json_ex;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static String firstid1;
    private static String secondid2;
    private static String result;
    private static String tar = "https://coinbin.org/";


    private static String TAG = "COIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText firstid = (EditText) findViewById(R.id.firstid);
        final EditText secondid = (EditText) findViewById(R.id.secondid);
        final Button clickbutton = (Button) findViewById(R.id.clickbutton);



        clickbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firstid1 = firstid.getText().toString();
                secondid2 = secondid.getText().toString();

                String requestURL = tar + firstid1 + "/to/" + secondid2 ;


                changeData task = new changeData();

                try {

                    result = task.execute(requestURL).get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    class changeData extends AsyncTask<String, Void, String> {


        TextView answer_rate = (TextView) findViewById(R.id.answer);

        String requestURL;

        @Override
        protected String doInBackground(String... params) {

            requestURL = params[0];
            String result;
            String temp;

            try {

                URL url = new URL(requestURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }

                bufferedReader.close();
                inputStream.close();


                result =  stringBuilder.toString().trim();

            } catch(Exception e){

                e.printStackTrace();
                result = null;
            }
            return result;
        }
        @Override
        public void onProgressUpdate (Void...values) { super.onProgressUpdate();}

        @Override
        protected void onPostExecute(String result) {


            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject rate = jsonObject.getJSONObject("coin");
                String answer = rate.getString("exchange_rate");

                answer_rate.setText(answer);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

