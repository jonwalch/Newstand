package com.newstand.news.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class Article implements Serializable {
    private URL url;
    private String title;
    private String source;
    private String image;
    private Bitmap img;

    public Article(String link, String title, String source, String image ) throws MalformedURLException{
        URL url = new URL(link);
        this.url = url;
        this.title = title;
        this.source = source;
        this.image = image;
        new DownloadImageTask().execute();
    }
    public URL getURL(){
        return this.url;
    }
    public String getTitle(){
        return this.title;
    }
    public String getSource(){
        return this.source;
    }
    public String getImage(){return this.image;}
    public Bitmap getImg(){return this.img;}


    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageTask() {
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Log.e("Starting","starting");
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = getImage();
            Log.e("url", urldisplay);
            img= null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return img;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            Log.e("Stopping","stopping");
        }
    }


}

