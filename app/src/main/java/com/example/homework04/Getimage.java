package com.example.homework04;
/*
File name: Getimage.java
Teammates: Bhanu Prakash Pamidi && Sai Koumudi Kaluvakolanu
Incalss 04
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

class Getimage extends AsyncTask<String,Void,Bitmap> {

    QuestionsGetter.Data  activity;


    public Getimage() {

    }

    public Getimage(QuestionsGetter.Data activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        activity.sendImage(bitmap);
        super.onPostExecute(bitmap);
    }

    @Override
    protected Bitmap doInBackground(String ... params) {
        try {


            URL url=new URL(params[0]);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Bitmap bitmap= BitmapFactory.decodeStream(con.getInputStream());
            if(bitmap==null){
                Log.d("demo","bitmap null");
            }
            return bitmap;
        }
        catch (Exception e){
            Log.d("demo",""+e.getMessage());
            e.printStackTrace();
        }


        return null;
    }
}
