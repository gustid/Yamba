package com.marakana.android.yamba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements
OnSharedPreferenceChangeListener{
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onStart(){
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //in carte nu scrie ce sa fac la metoda asta
        //trebuie neaparat sa o implementez
        //ca nu pot sa pun clasa abstracta
        //presupun ca trebuie sa modifc prefs cu sharedPreferences
        //nush ce sa fac cu key
        this.prefs = sharedPreferences;
    }
}
