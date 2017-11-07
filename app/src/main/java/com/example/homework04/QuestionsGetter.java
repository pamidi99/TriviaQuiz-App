package com.example.homework04;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class QuestionsGetter extends AsyncTask<String,Void,ArrayList<Question>> {

    public  static  interface Data{
        public void sendData(ArrayList<Question> questions);
        public  void sendImage(Bitmap bitmap);

    }
    Data data;

    public QuestionsGetter(Data data) {
        this.data=data;
    }

    @Override
    protected ArrayList<Question> doInBackground(String... params) {


        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb2=new StringBuilder();
            String line="";
            while ((line=br.readLine())!=null && !line.isEmpty()){
                sb2.append(line+"\n");
            }
            JSONObject jsonObject=new JSONObject(sb2.toString());
            JSONArray jsonArray=jsonObject.getJSONArray("questions");

            ArrayList<Question> questionsList=new ArrayList<Question>();

            for(int i=0;i<jsonArray.length();i++){
                JSONObject temp=(JSONObject)jsonArray.get(i);
                Question question=new Question();
                question.setText(temp.getString("text"));
                if(temp.has("image")) {
                    question.setImageUrl(temp.getString("image"));
                }
                JSONObject choicesObject=temp.getJSONObject("choices");
                question.setAnswer(Integer.parseInt(choicesObject.getString("answer"))-1);
               JSONArray choiceArray= choicesObject.getJSONArray("choice");
                ArrayList<String> answerList=new ArrayList<String>();
                for(int j=0;j<choiceArray.length();j++){
                    answerList.add(choiceArray.getString(j));
                       }
                    question.setChoices(answerList);
                questionsList.add(question);
            }
            return  questionsList;


        }

        catch (Exception e){
            Log.d("demo",""+e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Question> questions) {

        data.sendData(questions);

        super.onPostExecute(questions);
    }
}
