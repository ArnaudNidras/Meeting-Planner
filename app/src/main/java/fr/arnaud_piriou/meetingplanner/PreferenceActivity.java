package fr.arnaud_piriou.meetingplanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class PreferenceActivity extends android.preference.PreferenceActivity {



    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference languagePreference = getPreferenceScreen().findPreference(
                "switch_language");
        languagePreference.setOnPreferenceChangeListener(languageChangeListener);
    }

    Preference.OnPreferenceChangeListener languageChangeListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            boolean switched = ((SwitchPreference) preference).isChecked();

            if(switched){

                setLocale("fr");

            }
            else{

                setLocale("en");

            }

            return true;
        }
    };

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources ressources = getResources();
        DisplayMetrics dm = ressources.getDisplayMetrics();
        Configuration conf = ressources.getConfiguration();
        conf.locale = myLocale;
        ressources.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();

    }
}
