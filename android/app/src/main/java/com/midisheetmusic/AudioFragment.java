package com.midisheetmusic;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.zip.CRC32;

/**
 * Created by jason on 6/10/2015.
 */
public class AudioFragment extends Fragment {
    private int songIndex;
    private SongDBEntry dbEntry;
    private LinearLayout resultView;

    private FragmentActivity myContext;

    public static final String MidiTitleID = "MidiTitleID";
    public static final String LayoutID = "LayoutID";
    public static final int settingsRequestCode = 1;

    private MidiPlayer player;   /* The play/stop/rewind toolbar */
    private Piano piano;         /* The piano at the top */
    private SheetMusic sheet;    /* The sheet music */
    private LinearLayout layout; /* THe layout */
    private int layoutID;
    private MidiFile midifile;   /* The midi file to play */
    private MidiOptions options; /* The options for sheet music and sound */
    private long midiCRC;      /* CRC of the midi bytes */

    private ViewGroup container;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);


        Bundle args = getArguments();
        songIndex = args.getInt("songIndex", 0);

        DataBaseHelper myDBHelper = new DataBaseHelper(getActivity());
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

        FileUri file = dbEntry.getMidiFilename();
        String title = dbEntry.getTitle();

        byte[] data;
        try {
            data = file.getData(getActivity());
            if (data == null) {
                midifile = null;
            } else
                midifile = new MidiFile(data, title);
        }
        catch (MidiFileException e) {
            getActivity().finish();
            data = new byte[0];
            return;
        }

        if (midifile != null) {
            // Initialize the settings (MidiOptions).
            // If previous settings have been saved, used those
            options = new MidiOptions(midifile);
            CRC32 crc = new CRC32();
            crc.update(data);
            midiCRC = crc.getValue();
            SharedPreferences settings = getActivity().getPreferences(0);
            options.scrollVert = settings.getBoolean("scrollVert", true);
            options.shade1Color = settings.getInt("shade1Color", options.shade1Color);
            options.shade2Color = settings.getInt("shade2Color", options.shade2Color);
            options.showPiano = settings.getBoolean("showPiano", true);
            String json = settings.getString("" + midiCRC, null);
            MidiOptions savedOptions = MidiOptions.fromJson(json);
            if (savedOptions != null) {
                options.merge(savedOptions);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View resultView = inflater.inflate(R.layout.play_audio, container, false);

        TextView no_midi = (TextView)resultView.findViewById(R.id.no_midi);

        no_midi.setText(getString(R.string.no_midi));

        if (midifile == null)
            return resultView;

//        layout = new LinearLayout(this);


        no_midi.setVisibility(View.GONE);

        layout = (LinearLayout)resultView.findViewById(R.id.play_audio);

//        layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        player = new MidiPlayer(getActivity());
        piano = new Piano(getActivity());
        layout.addView(player);
//        layout.addView(piano);
//        getActivity().setContentView(layout);

        player.SetPiano(piano);
        layout.requestLayout();
        createSheetMusic(options);
//        sheet.setVisibility(View.GONE);

//        byte[] data = file.getData(container.getContext());
//        if (data == null || data.length <= 6 || !MidiFile.hasMidiHeader(data)) {
//            View rootView = inflater.inflate(R.layout.play_audio, container, false);
//        }


//        Intent intent = new Intent(Intent.ACTION_VIEW, file.getUri(), container.getContext(), SheetMusicActivity.class);

/*        layout = new LinearLayout(super.getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        player = new MidiPlayer(this);
        piano = new Piano(this);
        layout.addView(player);
        */
/*
        Intent intent = new Intent(Intent.ACTION_VIEW, file.getUri(), super.getActivity(), SheetMusicActivity.class);


        resultView = (LinearLayout) inflater.inflate(R.layout.play_audio, container, false);

        intent.putExtra(SheetMusicActivity.MidiTitleID, file.toString());
        intent.putExtra(SheetMusicActivity.LayoutID, R.layout.play_audio);
        startActivity(intent);
*/

        return resultView;
    }

    /** Create the SheetMusic view with the given options */
    private void
    createSheetMusic(MidiOptions options) {
        if (sheet != null) {
            layout.removeView(sheet);
        }
        if (!options.showPiano) {
            piano.setVisibility(View.GONE);
        }
        else {
            piano.setVisibility(View.VISIBLE);
        }
        sheet = new SheetMusic(getActivity());
//        sheet = new SheetMusic((Activity)this);
        sheet.init(midifile, options);
        sheet.setPlayer(player);
        layout.addView(sheet);
        piano.SetMidiFile(midifile, options, player);
        piano.SetShadeColors(options.shade1Color, options.shade2Color);
        player.SetMidiFile(midifile, options, sheet);
//        sheet.setVisibility(View.GONE);
//        player.setVisibility(View.GONE);
        layout.requestLayout();
        sheet.callOnDraw();
    }

    /** Always display this activity in landscape mode. */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (sheet != null)
                sheet.setVisibility(View.VISIBLE);
        } else {
            if (sheet != null)
                sheet.setVisibility(View.GONE);
        }
    }


    public void hideSheetMusic() {

    }

    public void showSheetMusic() {

    }

    /** When the menu button is pressed, initialize the menus. */
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (player != null) {
            player.Pause();
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sheet_menu, menu);
        return true;
    }
*/
    /** When this fragment pauses, stop the music */
    @Override
    public void onPause() {
        if (player != null) {
            player.Pause();
        }
        super.onPause();
    }

}