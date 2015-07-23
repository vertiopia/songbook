package com.midisheetmusic;

//import android.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

//import android.app.ActionBar;

/**
 * Created by jason on 6/5/2015.
 */
public class SongDisplayActivity extends ActionBarActivity implements ActionBar.TabListener {

    public static final String SongIndex = "SongIndex";
    public static final String SongTitle = "SongTitle";

    private int songIndex;
    private String songTitle;
//    private SongDBEntry dbEntry;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setTheme(android.R.style.Theme_Light);
        setContentView(R.layout.song_display);

        // Initialization
        viewPager = (ViewPager) findViewById(R.id.song_display);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        tabs = new String[]{ getString(R.string.lyrics_fragment), getString(R.string.audio_fragment)};

        this.songIndex = this.getIntent().getIntExtra(SongIndex, 0);
        mAdapter.setSongIndex(songIndex);

        viewPager.setAdapter(mAdapter);

        this.songTitle = this.getIntent().getStringExtra(SongTitle);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setTitle(songTitle);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        /**
                 * on swiping the viewpager make respective tab selected
                 **/
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
                if (position == 0) { // back to lyrics page
//                    View lyricView = viewPager.getChildAt(position);
                    Fragment lyricFragment = mAdapter.getItem(position);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

}
