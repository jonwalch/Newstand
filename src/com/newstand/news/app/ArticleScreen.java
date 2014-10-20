package com.newstand.news.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ArticleScreen extends Activity {
    TextView tview, title;
    Button add, remove;
    Article current;
    ImageView img;


    public Article getArticleMain(String name){
        for (int i = 0 ; i < MainActivity.articles.size(); i++){
            if(MainActivity.articles.get(i).getTitle().equals(name)){
                return MainActivity.articles.get(i);
            }
        }
        return null;
    }

    public Article getArticleFav(String name){
        for (int i = 0 ; i < FavoriteList.favorites.size(); i++){
            if(FavoriteList.favorites.get(i).getTitle().equals(name)){
                return FavoriteList.favorites.get(i);
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_screen);

        tview = (TextView) findViewById(R.id.textView);
        title = (TextView) findViewById(R.id.title);
        img = (ImageView) findViewById(R.id.imageView);
        add = (Button) findViewById(R.id.button);
        remove = (Button) findViewById(R.id.button2);

        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");
        String name = intent.getStringExtra("title");
        String splitter[] = name.split(System.getProperty("line.separator"));

        if(activity.equals("main")){
            current = getArticleMain(splitter[0]);
        }
        else if(activity.equals("fav")){
            current = getArticleFav(splitter[0]);
        }

        tview.setText(current.getURL().toString());
        title.setText(splitter[0]);
        tview.setAutoLinkMask(Linkify.ALL);
        if (current.getImg()!=null)
            img.setImageBitmap(current.getImg());
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(!FavoriteList.favorites.contains(current)){
                    FavoriteList.favorites.add(current);
                    // Intent myIntent = new Intent(ArticleScreen.this, MainActivity.class);
                    // ArticleScreen.this.startActivity(myIntent);
                }
                finish();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(FavoriteList.favorites.contains(current)){
                    FavoriteList.favorites.remove(current);
                    // Intent myIntent = new Intent(ArticleScreen.this, MainActivity.class);
                    //ArticleScreen.this.startActivity(myIntent);
                    // FavoriteList.popStringList();
                }
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        return;
    }

}
