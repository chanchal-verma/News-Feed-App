package com.chanchal.sindhubhawanshaadi.newsfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.prefs.Preferences;

public class SettingsNews extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_news);
    }

    public static class newsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        public void onCreate(Bundle savedInstancesState) {
            super.onCreate(savedInstancesState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference setionName = findPreference(getString(R.string.settings_sectionName_key));
            bindPreferenceSummaryToValue(setionName);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

        public boolean onPreferenceChange(Preference preference , Object value){
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }

    }
}