package com.marakana.android.yamba;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //check wether this activity was created before
        if(savedInstanceState == null){
            //Create a fragment
            SettingsFragment fragment = new SettingsFragment();

            //get fragment transaction from fragment manager and commit
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content,fragment,fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
