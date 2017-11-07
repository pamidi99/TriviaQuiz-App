package com.example.homework04;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Stats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Integer[] clickedanswers= (Integer[]) getIntent().getExtras().getSerializable("CLICKED_ANSWERS");
        LinearLayout layout = (LinearLayout) findViewById(R.id.scrollLinearVerticial);
        ArrayList<Question> qstnsList= (ArrayList<Question>) getIntent().getExtras().getSerializable("QUESTION");
        int count=qstnsList.size();
        for(int i=0;i<qstnsList.size();i++) {
            if(qstnsList.get(i).getAnswer()!=clickedanswers[i]) {
                count--;
                Question temp=qstnsList.get(i);
                View view = getLayoutInflater().inflate(R.layout.child_layout, layout, false);
                LinearLayout childLayout = (LinearLayout) view;
                TextView qstnText = (TextView) childLayout.getChildAt(0);
                TextView selectedAnswer = (TextView) childLayout.getChildAt(1);
                TextView correctAnswer = (TextView) childLayout.getChildAt(2);
                qstnText.setText(temp.getText());
                if(clickedanswers[i]!=-1){
                selectedAnswer.setText(temp.getChoices().get(clickedanswers[i]));}
                else{
                    selectedAnswer.setText("Not Answered");
                }
                correctAnswer.setText(temp.getChoices().get(temp.getAnswer()));
                layout.addView(view);


            }
        }
        ProgressBar pb= (ProgressBar) findViewById(R.id.progressBar);
        pb.setMax(qstnsList.size());
        pb.setProgress(count);
        TextView percentage= (TextView) findViewById(R.id.percentageCorrect);
        int percentagenumber=count*100/(qstnsList.size());
        percentage.setText(""+percentagenumber+"%");
        findViewById(R.id.finsih).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();

            }
        });
    }
}
