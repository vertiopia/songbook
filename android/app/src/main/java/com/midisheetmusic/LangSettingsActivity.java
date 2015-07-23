package com.midisheetmusic;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

/**
 * Created by jason on 7/14/2015.
 */
public class LangSettingsActivity extends Activity {

    private RadioGroup radioLangGroup;
    private RadioButton radioLangButton;
    private RadioButton radioEngButton;
    private RadioButton radioCTButton;
    private RadioButton radioCSButton;

    private Locale locale = null;
    private Configuration config = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_setting);
        config = getApplicationContext().getResources().getConfiguration();
        locale = config.locale;
        radioEngButton = (RadioButton) findViewById(R.id.radioEng);
        radioCTButton = (RadioButton) findViewById(R.id.radioCT);
        radioCSButton = (RadioButton) findViewById(R.id.radioCS);

        radioEngButton.setTextSize(convertFromDp(30));
        radioCTButton.setTextSize(convertFromDp(30));
        radioCSButton.setTextSize(convertFromDp(30));

/*
        radioEngButton.setPadding(0,6,0,6);
        radioCTButton.setPadding(0,6,0,6);
        radioCSButton.setPadding(0,6,0,6);
*/
        String lang = locale.getCountry();

        if ("US".equals(lang))
            radioEngButton.setChecked(true);
        if ("TW".equals(lang))
            radioCTButton.setChecked(true);
        if ("CN".equals(lang))
            radioCSButton.setChecked(true);

        addListenerOnButton();

    }

        public void addListenerOnButton() {

            radioLangGroup = (RadioGroup) findViewById(R.id.radioLang);
            radioLangGroup.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    int selectedId = radioLangGroup.getCheckedRadioButtonId();
                    radioLangButton = (RadioButton) findViewById(selectedId);


                }
            });


            radioEngButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    locale = new Locale("en", "US");
                    Locale.setDefault(locale);
                    config = getApplicationContext().getResources().getConfiguration();
                    config.locale = locale;
                    getApplicationContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//                    getParent().onConfigurationChanged(config);
//                    setContentView(R.layout.language_setting);

                    finish();
                }
            });

            radioCTButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    locale = new Locale("zh", "TW");
                    Locale.setDefault(locale);
                    config = getApplicationContext().getResources().getConfiguration();
                    config.locale = locale;
                    getApplicationContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//                    getParent().onConfigurationChanged(config);
//                    setContentView(R.layout.language_setting);

                    finish();
                }
            });

            radioCSButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    locale = new Locale("zh", "CN");
                    Locale.setDefault(locale);
                    config = getApplicationContext().getResources().getConfiguration();
                    config.locale = locale;
                    getApplicationContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    //getParent().onConfigurationChanged(config);
//                    setContentView(R.layout.language_setting);

                    finish();
                }
            });

        }

    public float convertFromDp(int input) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return ((input - 0.5f) / scale);
    }

    public float convertFromPx(int input) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (((float)input/scale) + 0.5f);
    }
}
