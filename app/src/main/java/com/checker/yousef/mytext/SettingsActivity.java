package com.checker.yousef.mytext;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.checker.yousef.mytext.library.Url;

public class SettingsActivity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);



        activity = this;

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();

    }

    public void openBrowser(String url,String title){
        Intent aboutApp = new Intent(SettingsActivity.this, BrowserActivity.class);
        aboutApp.putExtra("Url",url);
        aboutApp.putExtra("Title",title);
        startActivity(aboutApp);
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            findPreference("checklist").setOnPreferenceClickListener(new PreferenceListener());
            findPreference("history").setOnPreferenceClickListener(new PreferenceListener());
            findPreference("tutorial").setOnPreferenceClickListener(new PreferenceListener());
            findPreference("about").setOnPreferenceClickListener(new PreferenceListener());
            findPreference("aboutme").setOnPreferenceClickListener(new PreferenceListener());
            findPreference("feedback").setOnPreferenceClickListener(new PreferenceListener());
            findPreference("donate").setOnPreferenceClickListener(new PreferenceListener());
            findPreference("share").setOnPreferenceClickListener(new PreferenceListener());
            findPreference("star").setOnPreferenceClickListener(new PreferenceListener());
        }

        class PreferenceListener implements Preference.OnPreferenceClickListener{
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                switch (preference.getKey()){
                    case "checklist":
                        Toast.makeText(getActivity(), "You can visit here in the next version.", Toast.LENGTH_LONG).show();
                        break;
                    case "history":
                        Toast.makeText(getActivity(), "You can visit here in the next version.", Toast.LENGTH_LONG).show();
                        break;
                    case "tutorial":
                        Intent tutorial = new Intent(getActivity(), BrowserActivity.class);
                        tutorial.putExtra("Url", Url.tutorial);
                        tutorial.putExtra("Title","Tutorial");
                        startActivity(tutorial);
                        break;
                    case "about":
                        Intent about = new Intent(getActivity(), BrowserActivity.class);
                        about.putExtra("Url",Url.aboutapp);
                        about.putExtra("Title","About App");
                        startActivity(about);
                        break;
                    case "aboutme":
                        Intent aboutme = new Intent(getActivity(), BrowserActivity.class);
                        aboutme.putExtra("Url",Url.aboutme);
                        aboutme.putExtra("Title","About Me");
                        startActivity(aboutme);
                        break;
                    case "feedback":
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_EMAIL, "algha@outlook.com");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "From Word Checker(Android)");
                        intent.putExtra(Intent.EXTRA_TEXT, "Hello");

                        startActivity(Intent.createChooser(intent, "Send Email"));
                        break;
                    case "donate":
                        Intent donate = new Intent(getActivity(), DonateActivity.class);
                        startActivity(donate);
                        break;
                    case "share":
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareTitle = "Cool, this is a very good app!";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareTitle);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "url here");
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;
                    case "star":
                        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        break;
                }
                return true;
            }
        }

    }

}
