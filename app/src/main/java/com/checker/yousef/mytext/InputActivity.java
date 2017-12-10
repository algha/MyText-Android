package com.checker.yousef.mytext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.checker.yousef.mytext.library.CompareString;
import com.checker.yousef.mytext.library.Preferences;

public class InputActivity extends AppCompatActivity {

    private EditText sourceText;
    private EditText checkText;

    private Preferences preferences;
    private String newText = "";
    private final String checkTextKey = "checkText";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        preferences = new Preferences(this);

        Intent intent = getIntent();
        if (intent.getStringExtra("text") != null){
            newText = intent.getStringExtra("text");
        }
        initView();
    }


    private void initView(){
        sourceText = (EditText)findViewById(R.id.sourceText);
        checkText = (EditText)findViewById(R.id.checkText);
        sourceText.setText(newText);
        String check = preferences.getString(checkTextKey);

        if (check != null){
            checkText.setText(check);
        }

        findViewById(R.id.check).setOnClickListener(new clickListener());
    }

    public class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.check:

                    String checkString = checkText.getText().toString();
                    String sourceString = sourceText.getText().toString();

                    preferences.setString(checkTextKey,checkString);

                    CompareString compare = new CompareString(sourceString, checkString);
                    if (compare.getCount() > 0){
                        Intent intent = new Intent(InputActivity.this,OutputActivity.class);
                        intent.putExtra("newText", compare.getOutput());
                        intent.putExtra("count",compare.getCount());
                        startActivity(intent);
                    }else {
                        Toast.makeText(InputActivity.this, "No similar word", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

}
