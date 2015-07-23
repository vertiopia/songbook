package com.midisheetmusic;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.LeadingMarginSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by jason on 6/10/2015.
 */
public class LyricsFragment extends Fragment {

    private int songIndex;
    private SongDBEntry dbEntry;
    private ViewGroup container;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        this.container = container;
        songIndex = args.getInt("songIndex", 0);

        DataBaseHelper myDBHelper = new DataBaseHelper(container.getContext());
        try {
            myDBHelper.createDataBase();
        } catch (IOException e) {
            throw new Error("Error creating database");
        }

        try {
            myDBHelper.openDataBase();
        } catch (SQLiteException sqle) {
            throw sqle;
        }

        dbEntry = myDBHelper.fetchSong(songIndex);

        final TextView tv = new TextView(container.getContext());

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, convertFromDp(30));
        tv.setPadding(5, 5, 5, 5);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setText(createIndentedText(dbEntry.getLyrics(), 0, 20));
        myDBHelper.close();

        return tv;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        } else {
        }
    }

    static SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines), 0, text.length(), 0);
        return result;
    }

    public float convertFromDp(int input) {
        final float scale = container.getContext().getResources().getDisplayMetrics().density;
        return ((input - 0.5f) / scale);
    }

    public float convertFromPx(int input) {
        final float scale = container.getContext().getResources().getDisplayMetrics().density;
        return (((float)input/scale) + 0.5f);
    }

}
