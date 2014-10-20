package com.newstand.news.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.newstand.news.app.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class FavoriteList extends Activity implements Serializable {
    private static final long serialVersionUID = 0L;
    ListView list;
    static ArrayList<Article> favorites = new ArrayList<Article>();
    static ArrayList<String> stringList = new ArrayList<String>();
    static ArrayAdapter<String> arrayAdapter;
    public static final String storeFile = "list";

    public static void popStringList(){
        stringList.clear();
        for (int i = 0; i < favorites.size(); i++){
            stringList.add(favorites.get(i).getTitle() + "\n" + "(" + favorites.get(i).getSource() + ")");
        }
    }

    public static void writeApp(ArrayList<Article> list, Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(storeFile, context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(list);
        oos.close();
    }

    public static ArrayList<Article> readApp(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(storeFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<Article> list = (ArrayList<Article>) ois.readObject();
        ois.close();
        popStringList();
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        popStringList();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);
        Intent intent = getIntent();
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parentView, View childView,
                                    int position, long id) {

                Object selected = list.getItemAtPosition(position);
                Intent myIntent = new Intent(FavoriteList.this, ArticleScreen.class);
                myIntent.putExtra("title", selected.toString());
                myIntent.putExtra("activity","fav");
                FavoriteList.this.startActivity(myIntent);
                //setDetail(position);
            }

            public void onNothingSelected(AdapterView parentView) {

            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        popStringList();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);
        list.setAdapter(arrayAdapter);
        return;

    }
    protected void onStop(){
       super.onStop();
       try{
           writeApp(favorites, this.getApplicationContext());
       } catch (Exception e){
       }
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

}
