package com.damocles.sample;

import com.damocles.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class PreferenceActivitySample extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_preference);
        initUserName();
        initShare();
        initRate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_username")) {
            initUserName();
        }
    }

    private void initUserName() {
        EditTextPreference usernamePref;
        usernamePref = (EditTextPreference) findPreference("pref_username");
        String username = usernamePref.getText();

        if (username == null) {
            username = "?";
        }

        usernamePref.setSummary(String.format("Username: %s", username));
    }

    private void initShare() {
        Preference sharePref = findPreference("pref_share");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check this app!");
        intent.putExtra(Intent.EXTRA_TEXT, "Check this awesome app at: ...");
        sharePref.setIntent(intent);
    }

    private void initRate() {
        Preference ratePref = findPreference("pref_rate");
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ratePref.setIntent(intent);
    }

}
