package com.example.homework04;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class Trivia extends AppCompatActivity implements View.OnClickListener, QuestionsGetter.Data {
    ArrayList<Question> questionArrayList;
    int currentIndex = 0;
    Integer[] clickedChoices;
    int BUTTON_TAG_KEY = 1;
    CountDownTimer cdTimer;
    ProgressDialog pd;

    private GoogleApiClient client;


    @Override
    public void sendData(ArrayList<Question> questions) {

    }

    @Override
    public void sendImage(Bitmap bitmap) {
        //questionArrayList.get(currentIndex).setImage(bitmap);
        ImageView img = (ImageView) findViewById(R.id.imageView6);
        Log.d("demo", "img is set");
        img.setImageBitmap(bitmap);
        pd.dismiss();
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();

        if (tag.equals("prev")) {
            //for previous

            if (currentIndex != 0) {
                currentIndex--;
                Question currentQuestion = questionArrayList.get(currentIndex);
                if (currentQuestion.getImage() == null && !currentQuestion.getImageUrl().isEmpty()) {
                    // add a progress spinner for image here
                    pd = new ProgressDialog(Trivia.this, R.style.MyTheme);
                    pd.setCancelable(false);
                    pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    //pd.setTitle("Loading Image");
                    String title = "Loading Image";
                    SpannableString ss1 = new SpannableString(title);
                    ss1.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, ss1.length(), 0);
                    pd.setTitle(ss1);
                    pd.getWindow().setGravity(Gravity.TOP);
                    WindowManager.LayoutParams params = pd.getWindow().getAttributes();
                    params.y = 150;
                    pd.getWindow().setAttributes(params);
                    pd.show();
                    new Getimage(Trivia.this).execute(currentQuestion.getImageUrl());


                }
                displayCurrentQstn();
            }

        } else if (tag.equals("finish")) {
            Intent intent = new Intent(Trivia.this, Stats.class);
            intent.putExtra("CLICKED_ANSWERS", clickedChoices);
            intent.putExtra("QUESTION", questionArrayList);
            startActivity(intent);
            //for finish
        } else {
            // for next

            currentIndex++;
            Question currentQuestion = questionArrayList.get(currentIndex);
            if (currentQuestion.getImage() == null && !currentQuestion.getImageUrl().isEmpty()) {
                // add a progress spinner for image here
                pd = new ProgressDialog(Trivia.this, R.style.MyTheme);
                pd.setCancelable(false);

                pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                pd.getWindow().setGravity(Gravity.TOP);
                WindowManager.LayoutParams params = pd.getWindow().getAttributes();
                params.y = 150;
                pd.getWindow().setAttributes(params);
                //pd.setTitle("Loading Image");
                String title = "Loading Image";
                SpannableString ss1 = new SpannableString(title);
                ss1.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, ss1.length(), 0);
                pd.setTitle(ss1);
                pd.show();
                new Getimage(Trivia.this).execute(currentQuestion.getImageUrl());

            }
            displayCurrentQstn();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        questionArrayList = (ArrayList<Question>) getIntent().getExtras().getSerializable("QUESTION_KEY");
        clickedChoices = new Integer[questionArrayList.size()];

        for (int i = 0; i < clickedChoices.length; i++) {
            clickedChoices[i] = -1;
        }
        currentIndex = 0;
        RadioGroup rg = (RadioGroup) findViewById(R.id.answersRG);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                clickedChoices[currentIndex] = checkedId;
            }
        });
        Question currentQstn = questionArrayList.get(currentIndex);
        if (!currentQstn.getImageUrl().isEmpty()) {
            pd = new ProgressDialog(Trivia.this, R.style.MyTheme);
            pd.setCancelable(false);
            pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            //pd.setTitle("Loading Image");
            String title = "Loading Image";
            SpannableString ss1 = new SpannableString(title);
            ss1.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, ss1.length(), 0);
            pd.setTitle(ss1);


            pd.getWindow().setGravity(Gravity.TOP);
            WindowManager.LayoutParams params = pd.getWindow().getAttributes();
            params.y = 150;
            pd.getWindow().setAttributes(params);
            pd.show();
            new Getimage(Trivia.this).execute(currentQstn.getImageUrl());
        }
        displayCurrentQstn();
        cdTimer = new CountDownTimer(120000l, 1000l) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView timer = (TextView) findViewById(R.id.timeLeftId);
                int secLeft = (int) (millisUntilFinished / 1000);
                timer.setText("Time Left: " + secLeft + " Seconds");
            }

            @Override
            public void onFinish() {
                //write intent to call stats activity
                Intent intent = new Intent(Trivia.this, Stats.class);
                intent.putExtra("CLICKED_ANSWERS", clickedChoices);
                intent.putExtra("QUESTION", questionArrayList);
                startActivity(intent);
                finish();
            }
        };
        cdTimer.start();

        findViewById(R.id.buttonPrevious).setOnClickListener(this);
        findViewById(R.id.buttonNext).setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void displayCurrentQstn() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.answersRG);
        rg.removeAllViews();
        Question currentquestion = questionArrayList.get(currentIndex);
        ArrayList<String> choices = currentquestion.getChoices();
        for (int i = 0; i < choices.size(); i++) {
            RadioButton rb = new RadioButton(Trivia.this);
            rb.setText(choices.get(i));
            rb.setId(i);
            rg.addView(rb);

            if (i == clickedChoices[currentIndex]) {
                rb.setChecked(true);
            }
        }

        TextView qstn = (TextView) findViewById(R.id.textView3);
        qstn.setText(currentquestion.getText());
        qstn.setTypeface(qstn.getTypeface(), Typeface.BOLD);
        TextView qstnid = (TextView) findViewById(R.id.qstnId);
        int displayQuestionId = currentIndex + 1;
        qstnid.setText("Q" + displayQuestionId);
        if (currentquestion.getImage() != null) {
            ImageView img = (ImageView) findViewById(R.id.imageView6);
            img.setImageBitmap(currentquestion.getImage());
        } else {
            ImageView img = (ImageView) findViewById(R.id.imageView6);
            img.setImageResource(R.color.transparent);
        }
        if (currentIndex != questionArrayList.size() - 1) {
            Button finish = (Button) findViewById(R.id.buttonNext);
            finish.setText("Next");
            finish.setTag("next");
        } else {
            Button finish = (Button) findViewById(R.id.buttonNext);
            finish.setText("Finish");
            finish.setTag("finish");

        }
        Button prev = (Button) findViewById(R.id.buttonPrevious);
        prev.setText("Previous");
        prev.setTag("prev");
        if (currentIndex <= 0) {
            prev.setEnabled(false);
        } else
            prev.setEnabled(true);
        //prev.setTag(BUTTON_TAG_KEY,getResources().getString(R.string.prev));

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Trivia Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
