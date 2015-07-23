package com.midisheetmusic;

/**
 * Created by jason on 6/4/2015.
 */
public class SongDBEntry {
    public int songIndex;
    public String songName;
    public String songLyrics;
    public FileUri midiFilename;


    SongDBEntry(int index, String name, FileUri filename) {
        songIndex = index;
        songName = name;
        midiFilename = filename;
    }

    public void setLyrics(String inputLyrics) {
        songLyrics = inputLyrics;
    }

    public int getID() {
        return songIndex;
    }

    public String getTitle() {
        return songName;
    }

    public String getLyrics() {return songLyrics; }

    public FileUri getMidiFilename() {
        return midiFilename;
    }

}
