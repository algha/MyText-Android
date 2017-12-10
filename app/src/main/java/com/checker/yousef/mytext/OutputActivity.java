package com.checker.yousef.mytext;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class OutputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);


        Intent intent = getIntent();
        String newText = intent.getStringExtra("newText");
        Integer wordCount = intent.getIntExtra("count",0);

        TextView outputText = (TextView)findViewById(R.id.outputText);
        outputText.setText(Html.fromHtml(newText));

        TextView outputCount = (TextView)findViewById(R.id.outputCount);
        outputCount.setText("Founded word is:"+wordCount);
    }




}
