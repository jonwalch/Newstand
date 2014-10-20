package com.newstand.news.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.newstand.news.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class MainActivity extends Activity {

    Button favButton, searchButton;
    ListView list;
    EditText search;
    static ArrayList<Article> articles = new ArrayList<Article>();
    ArrayList<String> pop = new ArrayList<String>();
    String query = "";
    ArrayAdapter<String> arrayAdapter;

    private class Async extends AsyncTask<Void,Void,String> {

        private Exception exception;

        protected String doInBackground(Void... urls){
            try{
                populateArticle(query);
                popStringList();

            } catch (Exception e){
                this.exception = e;

            }
            return "";
        }

        protected void onPostExecute(String result) {
            arrayAdapter.notifyDataSetChanged();

        }
    }

    private static String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();
    }
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    public void populateArticle(String query) throws IOException, JSONException{
        JSONObject json = readJsonFromUrl("http://www.faroo.com/api?q="+query+"&start=1&l=en&src=news&f=json&key=veqjHxYB-Kugzu7sMdA6YcNgeIc_");
        JSONArray list = json.getJSONArray("results");
        articles.clear();

        for (int i = 0; i < list.length(); i++) {

            Article temp = new Article(((JSONObject)list.get(i)).getString("url"), ((JSONObject)list.get(i)).getString("title"), ((JSONObject)list.get(i)).getString("domain"), ((JSONObject)list.get(i)).getString("iurl"));
            articles.add(temp);
        }
    }
    public void popStringList(){
        pop.clear();
        for (int i = 0; i < articles.size(); i++){
            pop.add(articles.get(i).getTitle() + "\n" +"("+ articles.get(i).getSource()+")");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pop);

        list = (ListView) findViewById(R.id.listView);
        favButton = (Button) findViewById(R.id.button2);
        searchButton = (Button) findViewById(R.id.button);

        new Async().execute();

        list.setAdapter(arrayAdapter);

        try {
            FavoriteList.favorites = FavoriteList.readApp(this.getApplicationContext());
        } catch (Exception e) {
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                search = (EditText) findViewById(R.id.editText);
                query = search.getText().toString();
                query = query.replace(" ","%20");
                new Async().execute();
                list.setAdapter(arrayAdapter);

            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent myIntent = new Intent(MainActivity.this, FavoriteList.class);

                MainActivity.this.startActivity(myIntent);

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parentView, View childView,
                                    int position, long id) {

                Object selected = list.getItemAtPosition(position);
                Intent myIntent = new Intent(MainActivity.this, ArticleScreen.class);
                myIntent.putExtra("title", selected.toString());
                myIntent.putExtra("activity","main");
                MainActivity.this.startActivity(myIntent);

            }

            public void onNothingSelected(AdapterView parentView) {

            }
        });

    }
}