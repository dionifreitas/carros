package br.com.livroandroid.carros.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by thiago on 09/10/16.
 */

public class PrefsUtils {
    public static boolean isCheckPushOn(final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("PREF_CHECK_PUSH",false);
    }
}
