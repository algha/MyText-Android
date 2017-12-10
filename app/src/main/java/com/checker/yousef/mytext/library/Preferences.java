package com.checker.yousef.mytext.library;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yousef on 2/12/2017.
 */

public class Preferences {

    private SharedPreferences prefs;

    public Preferences(Context context){
        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public String getString(String key){
        return prefs.getString(key,"");
    }

    public void setString(String key,String val){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,val);
        editor.apply();
    }

}
