package com.marakana.android.yamba;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;


import java.util.List;

import winterwell.jtwitter.Twitter;

public class RefreshService extends IntentService{

    static final String TAG = "RefreshService";
    public RefreshService() {
        super(TAG);
    }

    //a method that is called once
    //when the service is created
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"onCreate");
    }

    //Executes separately on a working thread
    //After execution onDestry() is called
    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = prefs.getString("username","");
        final String password = prefs.getString("password","");

        //Check the username and password are not empty
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"Check username and password",Toast.LENGTH_LONG).show();
            return;
        }
        //System.out.println(username);
        //System.out.println(password);
        Log.d(TAG,"onStarted");


        ContentValues values = new ContentValues();
        YambaClient cloud = new YambaClient(username,password,"http://yamba.newcircle.com/api");
        try {
            int count = 0;
            List<Status> timeline = cloud.getTimeline(20);

            for (Status status : timeline){
                values.clear();
                values.put(StatusContract.Column.ID,status.getId());
                values.put(StatusContract.Column.USER,status.getUser());
                values.put(StatusContract.Column.MESSAGE,status.getMessage());
                values.put(StatusContract.Column.CREATED_AT,status.getCreatedAt().getTime());
                //db.insertWithOnConflict(StatusContract.TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);
                Uri uri = getContentResolver().insert(StatusContract.CONTENT_URI, values);
                if(uri!=null){
                    count++;
                    Log.d(TAG,
                            String.format("%s: %s",status.getUser(),status.getMessage()));
                    System.out.println("user " + status.getUser() + " message "+ status.getMessage());
                }
            }
            if (count>0){
                sendBroadcast(new Intent(
                        "com.marakana.android.yamba.action.NEW_STATUSES"
                ).putExtra("count",count));
            }
        }catch (YambaClientException e){
            Log.e(TAG,"Failed to fetch timeline");
            e.printStackTrace();
        }
    }

    //called once, when the service is destroyed
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
