package com.marakana.android.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
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
        System.out.println(username);
        System.out.println(password);
        Log.d(TAG,"onStarted");

        //YambaClient cloud = new YambaClient(username, password,"http://yamba.newcircle.com/api");
        try {
            //YambaClient cloud = new YambaClient(username, password,"http://yamba.newcircle.com/api");
            Twitter cloud = new Twitter(username,password);
            cloud.setAPIRootUrl("http://yamba.newcircle.com/api");
            List<Twitter.Status> timeline = cloud.getPublicTimeline();
            System.out.println(timeline.size());
            for (Twitter.Status status : timeline){
                Log.d(TAG,String.format("%s:%s", status.getUser(),status.getText()));
            }
        }catch (Exception e){
            Log.e(TAG,"Failed to fetch timeline");
            e.printStackTrace();
        }

        try {
            //YambaClient cloud = new YambaClient(username, password,"http://yamba.newcircle.com/api");
            YambaClient cloud = new YambaClient(username,password,"http://yamba.newcircle.com/api");

            List<Status> timeline = cloud.getTimeline(20);
            System.out.println(timeline.size());
            for (Status status : timeline){
                Log.d(TAG,String.format("%s:%s", status.getUser(),status.getMessage()));
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

/*
public class RefreshService extends Service {
    static final String TAG = "RefreshService";

    //used when we have binded services
    //services that have a lifecycle which depends on the livecycle of creating activities
    //not the case here
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //a method that is called once
    //when the service is created
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"onCreate");
    }

    //called each time a service is needed
    //doesn't call onCreate()
    //doesn't call onDestroy()
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent,flags,startId);
        Log.d(TAG,"onStarted");
        return START_STICKY;
    }

    //called once, when the service is destroyed
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}*/
