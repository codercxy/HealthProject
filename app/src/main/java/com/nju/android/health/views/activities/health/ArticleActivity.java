package com.nju.android.health.views.activities.health;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Article;

public class ArticleActivity extends AppCompatActivity {
    private ImageView img;
    private TextView titleText;
    private TextView authorText;
    private TextView timeText;
    private TextView contentText;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initView();

        initData();
    }
    private void initView() {
        img = (ImageView) findViewById(R.id.article_img);
        titleText = (TextView) findViewById(R.id.article_title);
        authorText = (TextView) findViewById(R.id.article_author);
        timeText = (TextView) findViewById(R.id.article_time);
        contentText = (TextView) findViewById(R.id.article_content);
    }

    private void initData() {
        article = (Article) getIntent().getParcelableExtra("article");

        img.setImageResource(article.getImgRes());
        titleText.setText(article.getTitle());
        authorText.setText(article.getAuthor());
        timeText.setText(article.getDate());
        contentText.setText(article.getContent());
    }
}
