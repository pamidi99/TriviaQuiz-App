package com.example.homework04;
/*
HomeWork04
Names: Bhanu Prakash Pamidi && Sai Koumudi Kaluvakolanu
File name: MainActivity.java
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements QuestionsGetter.Data {

    ArrayList<Question> questionArrayList;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button exitButton=(Button)findViewById(R.id.exitButton);
        Button startTriviaButton=(Button)findViewById(R.id.startTreviaButton);
        startTriviaButton.setEnabled(false);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pd = new ProgressDialog(MainActivity.this,R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

       // pd.setTitle("Loading Trivia");
        String title = "Loading Trivia";
        SpannableString ss1 = new SpannableString(title);
        ss1.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, ss1.length(), 0);
        pd.setTitle(ss1);
        pd.show();

        new QuestionsGetter(MainActivity.this).execute("http://dev.theappsdr.com/apis/trivia_json/index.php");
        findViewById(R.id.startTreviaButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Trivia.class);
                intent.putExtra("QUESTION_KEY",questionArrayList);
                startActivity(intent);

            }
        });


    }

    @Override
    public void sendData(ArrayList<Question> questions) {

        this.questionArrayList=questions;
        pd.dismiss();
        Button startTriviaButton=(Button)findViewById(R.id.startTreviaButton);
        startTriviaButton.setEnabled(true);

        ImageView iv=(ImageView)findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.trivia);
        TextView ready=(TextView)findViewById(R.id.Ready);
        ready.setText("Trivia Ready");
       // ready.setGravity(Gravity.CENTER);

    }

    @Override
    public void sendImage(Bitmap bitmap) {

    }
}

