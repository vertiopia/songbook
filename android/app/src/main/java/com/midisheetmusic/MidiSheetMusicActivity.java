/*
 * Copyright (c) 2011-2012 Madhav Vaidyanathan
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */

package com.midisheetmusic;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/** @class MidiSheetMusicActivity
 * This is the launch activity for MidiSheetMusic.
 * It simply displays the splash screen, and a button to choose a song.
 */
public class MidiSheetMusicActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadImages();
        setContentView(R.layout.main);

        /*
        Button chooseSongButton = (Button) findViewById(R.id.choose_song);
        chooseSongButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        chooseSong();
                    }
                }
        );
*/

        ImageButton langSettingButton = (ImageButton) findViewById(R.id.lang_setting);
        ViewGroup.LayoutParams params = langSettingButton.getLayoutParams();
        params.height = (int) convertFromPx(75);
        params.width = (int) convertFromPx(75);
        langSettingButton.setLayoutParams(params);
        langSettingButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        langSetting();
                    }
                }
        );


        Button atozButton = (Button) findViewById(R.id.a_to_z);
        atozButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, convertFromDp(40));
        atozButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        atoz();
                    }
                }
        );
        /*
        Button categoriesButton = (Button) findViewById(R.id.categories);
        categoriesButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        chooseSong();
                    }
                }
        );
        */
        Button byNumberButton = (Button) findViewById(R.id.by_number);
        byNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, convertFromDp(40));
        byNumberButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        number();
                    }
                }
        );

/*
        Button titleSearchButton = (Button) findViewById(R.id.title_search);

        titleSearchButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        chooseSong();
                    }
                }
        );
*/
        Button lyricsSearchButton = (Button) findViewById(R.id.lyrics_search);
        lyricsSearchButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, convertFromDp(40));
        lyricsSearchButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        lyricSearch();
                    }
                }
        );


    }

    /** Start the ChooseSongActivity when the "Choose Song" button is clicked */
    private void chooseSong() {
        Intent intent = new Intent(this, ChooseSongActivity.class);
        startActivity(intent);
    }

    /** Start the LangSettingActivity when the "Lang Setting" button is clicked */
    private void langSetting() {
        Intent intent = new Intent(this, LangSettingsActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        setContentView(R.layout.main);
//        View v = findViewById(R.layout.main);
        restartActivity();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /** Start the AToZActivity when the "A-Z" button is clicked */
    private void atoz() {
        Intent intent = new Intent(this, AToZActivity.class);
        startActivity(intent);
    }

    /** Start the NumberActivity when the "Number" button is clicked */
    private void number() {
        Intent intent = new Intent(this, NumberActivity.class);
        startActivity(intent);
    }

    /** Start the NumberActivity when the "Number" button is clicked */
    private void lyricSearch() {
        Intent intent = new Intent(this, LyricSearchActivity.class);
        startActivity(intent);
    }

    /** Load all the resource images */
    private void loadImages() {
        ClefSymbol.LoadImages(this);
        TimeSigSymbol.LoadImages(this);
        MidiPlayer.LoadImages(this);
    }

    /** Always use landscape mode for this activity. */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public float convertFromDp(int input) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((input - 0.5f) / scale);
    }

    public float convertFromPx(int input) {
        final float scale = getResources().getDisplayMetrics().density;
        return (((float)input/scale) + 0.5f);
    }
}

