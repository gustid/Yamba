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

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class StatusActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.status,menu);
        return true;
    }
}
