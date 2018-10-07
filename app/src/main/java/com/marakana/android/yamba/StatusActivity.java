package com.marakana.android.yamba;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;


import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusActivity extends AppCompatActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        editStatus = (EditText)findViewById(R.id.editStatus);
        buttonTweet = (Button)findViewById(R.id.buttonTweet);
        textCount = (TextView)findViewById(R.id.textCount);

        buttonTweet.setOnClickListener(this);

        defaultTextColor = textCount.getTextColors().getDefaultColor();
        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = 140 - editStatus.length();
                textCount.setText(Integer.toString(count));
                textCount.setTextColor(Color.GREEN);
                if(count < 10)
                    textCount.setTextColor(Color.RED);
                else
                    textCount.setTextColor(Color.GREEN);
            }
        });
    }

    @Override
    public void onClick(View view){
        String status = editStatus.getText().toString();
        Log.d(TAG, "onClicked with status: "+ status);

        //executa intr-un alt task/thread in background
        new PostTask().execute(status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        //This adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.status,menu);
        return true;
    }

    private final class PostTask extends AsyncTask<String, Void,String>{
        @Override
        protected String doInBackground(String... params){
            YambaClient yambaClient = new YambaClient("student","password");
            try{
                yambaClient.postStatus(params[0]);
                return "Succesfully posted";
            }catch (YambaClientException e){
                e.printStackTrace();
                return "Failed to post yamba service";
            }
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this,result,Toast.LENGTH_LONG).show();
        }
    }

    private EditText editStatus;
    private Button buttonTweet;
    private static final String TAG = "StatusActivity";
    private TextView textCount;
    private int defaultTextColor;
}
