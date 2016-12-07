package br.com.livroandroid.carros.activity.prefs;


import android.os.Bundle;

import br.com.livroandroid.carros.R;

@SuppressWarnings("deprecation")
public class ConfiguracoesActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
