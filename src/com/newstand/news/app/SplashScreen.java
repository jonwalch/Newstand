package com.newstand.news.app;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.newstand.news.app.R;

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 3000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new BackgroundSplashTask().execute();
    }

    private class BackgroundSplashTask extends AsyncTask<Void,Void,Void>{
        protected void onPreExecute(){
            super.onPreExecute();
        }
        protected Void doInBackground(Void... arg0){
            try{
                Thread.sleep(SPLASH_TIME_OUT);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            Intent i = new Intent(SplashScreen.this,MainActivity.class);
            i.putExtra("loaded_info", " ");
            startActivity(i);
            finish();
        }
    }
}
