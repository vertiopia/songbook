package com.midisheetmusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by jason on 6/10/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    private int songIndex = 0;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    void setSongIndex(int index) {
        songIndex = index;
    }

    @Override
    public Fragment getItem(int index) {

        Bundle args = new Bundle();
        args.putInt("songIndex", songIndex);

        switch (index) {
            case 0:
                // Display Lyrics Fragment
                LyricsFragment lf = new LyricsFragment();
                lf.setArguments(args);
                return lf;
            case 1:
                // Play Audio Fragment
                AudioFragment af = new AudioFragment();
                af.setArguments(args);
                return af;
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}
