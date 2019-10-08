package com.example.capstondesign_team_cs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.capstondesign_team_cs.db.SimpleDB;
import com.example.capstondesign_team_cs.vo.ArticleVO;

public class ArticleActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_article);

        prepareSimpleDB();

        LinearLayout ll = (LinearLayout)findViewById(R.id.itemList);

        for (int i = 0; i < SimpleDB.getIndexes().size(); i++) {
            Button button = new AppCompatButton(this);
            button.setText(SimpleDB.getIndexes().get(i));

            ll.addView(button);
        }
    }

    private void prepareSimpleDB() {
        for (int i =1; i<100; i++) {
            SimpleDB.addArticle(i+ "번글", new ArticleVO(i, i + "번글 제목", i + "번글 내용", "내가"));
        }
    }
}
