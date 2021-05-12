package com.myappcompany.proprakhar.weatherdetector;
//I download the image from unsplash.com
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    String place;
    TextView resultTextView;
    public class DownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                String result="";
                URL url = new URL(urls[0]);
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                InputStream in=connection.getInputStream();
                InputStreamReader reader =new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char) data;
                    result+=current;
                    data=reader.read();
                }
                //  Log.i("Json","yes");
                return result;

            }
            catch(Exception e)
            {

               //Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
               // Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return null;
            }
            //It should never touch anything from  the ui perspective.
            //this is all supposed to happen on the background while on postexecute this is where you could go touch something in the user interface so we are not going to do that right now.
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //And this is basically our chance to write some code when this has finished running.
            //this is the place where you could go touch something in the user interface
           //It will give you the json
            try {
                Log.i("Json",s);
                JSONObject jsonObject = new JSONObject(s);
                //for getting only the weather.
                String weatherInfo=jsonObject.getString("weather");
                String mainInfo=jsonObject.getString("main");//weather
                String correct="[" +mainInfo+"]";//my
                 Log.i("weather content",weatherInfo);
                Log.i("weather content",mainInfo);
                JSONArray arr=new JSONArray(weatherInfo);
                JSONArray ar=new JSONArray(correct);
                Log.i("Json",arr.toString());
                Log.i("Json",ar.toString());
                Log.i("Json length",Integer.toString(arr.length()));
                Log.i("Json length",Integer.toString(arr.length()));
                String message="";
                for(int i=0;i<ar.length();i++)
                {
                    //  Log.i("Hey","It's us");

                    JSONObject jonPart =ar.getJSONObject(i);
                    String temp=jonPart.getString("temp");
                    String feelsLike=jonPart.getString("feels_like");
                    // Log.i("main",jsonPart.getString("main"));
                    // Log.i("description",jsonPart.getString("description"));
                    if(!temp.isEmpty()&&!feelsLike.isEmpty()) {
                        message += "Temp :" + temp + "'C\r\n"+ "FeelsLike :"+ feelsLike+"'C\r\n";
                    }
                    }
                for(int i=0;i<arr.length();i++)
                {
                    //  Log.i("Hey","It's us");

                    JSONObject jsonPart =arr.getJSONObject(i);
                    String main=jsonPart.getString("main");
                    String description=jsonPart.getString("description");
                   // Log.i("main",jsonPart.getString("main"));
                   // Log.i("description",jsonPart.getString("description"));
                    if(!main.isEmpty()&&!description.isEmpty())
                        message += main + ": " + description+"\r\n";
                }
                if(!message.equals(""))
                {
                    resultTextView.setText(message);
                }
                else
                {
                  Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void getWeather (View view) {
        try {
            resultTextView.setText("");
            DownloadTask task = new DownloadTask();
            String encodedCityName=URLEncoder.encode(editText.getText().toString(),"UTF-8");
           // String encodeCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
           // String encodeCityName = editText.getText().toString();
          //  String url = "https://samples.openweathermap.org/data/2.5/weather?q=" + encodeCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02a";
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02");//this is url link of the page,and that page contains  uk london api or weather info in json format.
           InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
           mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);

        }catch(Exception e){

             Toast.makeText(getApplicationContext(),"Could not find weather:)",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
//public void getWeather(View view){
//    try {
//        DownloadTask task = new DownloadTask();
//     //   String encodeCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
//        String encodeCityName = editText.getText().toString();
//        task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodeCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02");
//
//      //  InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//     //   mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
//    }catch (Exception e){
//        e.printStackTrace();
//        Toast.makeText(getApplicationContext(),"Could not find weather:)",Toast.LENGTH_SHORT).show();
//
//    }
//}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DownloadApi task=new DownloadApi();
//        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=439d4b804bc8187953eb36d2a8c26a02a");//this is url link of the page,and that page contains  uk london api or weather info in json format.
        editText=findViewById(R.id.editText);
        resultTextView=findViewById(R.id.resultTextView);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
    }
}
