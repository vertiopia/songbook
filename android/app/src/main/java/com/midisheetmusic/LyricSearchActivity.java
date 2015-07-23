package com.midisheetmusic;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jason on 6/3/2015.
 */

/** @class AllSongsActivity
 * The AllSongsActivity class is used to display a list of
 * songs to choose from.  The list is created from the songs
 * shipped with MidiSheetMusic (in the assets directory), and
 * also by searching for midi files in the internal/external
 * device storage.
 *
 * When a song is chosen, this calls the SheetMusicAcitivty, passing
 * the raw midi byte[] data as a parameter in the Intent.
 */

public class LyricSearchActivity extends ListActivity implements TextWatcher {

    /** The complete list of midi files */
    ArrayList<FileUri> songlist;
    ArrayList<SongDBEntry> midiSonglist;

    /** Textbox to filter the songs by name */
    EditText filterText;

    SongDBLyricArrayAdapter<SongDBEntry> adapter;

    /* When this activity changes orientation, save the songlist,
     * so we don't have to re-scan for midi songs.
     */
//    @Override
//    public Object onRetainNonConfigurationInstance() {
//        return songlist;
//    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.choose_song);
        setTitle(getString(R.string.app_name) + ": Choose Song");
		/* If we're restarting from an orientation change,
		 * load the saved song list.
		 */
        midiSonglist = (ArrayList<SongDBEntry>) getLastNonConfigurationInstance();
        if (midiSonglist != null) {
            adapter = new SongDBLyricArrayAdapter<SongDBEntry>(this, android.R.layout.simple_list_item_1, midiSonglist);
            this.setListAdapter(adapter);
        }

//        songlist = (ArrayList<FileUri>) getLastNonConfigurationInstance();
//        if (songlist != null) {
//            adapter = new IconArrayAdapter<FileUri>(this, android.R.layout.simple_list_item_1, songlist);
//            this.setListAdapter(adapter);
//        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (midiSonglist == null || midiSonglist.size() == 0) {
            midiSonglist = new ArrayList<SongDBEntry>();
            loadAssetMidiFiles();

            adapter = new SongDBLyricArrayAdapter<SongDBEntry>(this, android.R.layout.simple_list_item_1, midiSonglist);
            this.setListAdapter(adapter);
        }
        filterText = (EditText) findViewById(R.id.name_filter);
        filterText.setBackgroundColor(Color.WHITE);
        filterText.setTextColor(Color.BLACK);
        filterText.addTextChangedListener(this);
        filterText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        // hide keyboard by default
        filterText.clearFocus();
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(filterText.getWindowToken(), 0);
    }


    /** Load all the sample midi songs from the assets directory into songlist.
     *  Look for files ending with ".mid"
     */
    void loadAssetMidiFiles() {

        DataBaseHelper myDBHelper = new DataBaseHelper(this);
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

        ArrayList<SongDBEntry> dbSonglist = myDBHelper.listSongs();
        midiSonglist.addAll(dbSonglist);

        myDBHelper.close();

    }


    /** When a song is clicked on, start a SheetMusicActivity.
     *  Read the raw byte[] data of the midi file.
     *  Pass the raw byte[] data as a parameter in the Intent.
     *  Pass the midi file Title as a parameter in the Intent.
     */
    @Override
    protected void onListItemClick(ListView parent, View view, int position, long id) {
        super.onListItemClick(parent, view, position, id);

        SongDBEntry dbEntry = (SongDBEntry) this.getListAdapter().getItem(position);

        int songID = dbEntry.getID();
        String songTitle = dbEntry.getTitle();
        FileUri file = dbEntry.getMidiFilename();

        byte[] data = file.getData(this);
        if (data == null || data.length <= 6 || !MidiFile.hasMidiHeader(data)) {
            //AToZActivity.showErrorDialog("Error: Unable to open song: " + file.toString(), this);
//            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, file.getUri(), this, SongDisplayActivity.class);
        intent.putExtra(SongDisplayActivity.SongIndex, songID);
        intent.putExtra(SongDisplayActivity.SongTitle, songTitle);
        startActivity(intent);

        /*
                Intent intent = new Intent(Intent.ACTION_VIEW, file.getUri(), this, SheetMusicActivity.class);
                intent.putExtra(SheetMusicActivity.MidiTitleID, file.toString());
                startActivity(intent);
                */


    }


    /** As text is entered in the filter box, filter the list of
     *  midi songs to display.
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }


}
