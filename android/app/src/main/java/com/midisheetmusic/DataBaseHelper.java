package com.midisheetmusic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by jason on 5/20/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/com.midisheetmusic/databases/";
    private static String DB_NAME = "SJCAMIDIdb";

    private static final String TAG = DataBaseHelper.class.getSimpleName();

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public static final String KEY_ID = "_id";
    public static final String MIDITABLE = "SongDB";
    public static final String ETITLE = "eTitle";
    public static final String CTTITLE = "ctTitle";
    public static final String CSTITLE = "csTitle";
    public static final String CATEGORY = "Cat";
    public static final String SUBCATEGORY = "SubCat";
    public static final String ELYRICS = "eLyrics";
    public static final String MIDIFILENAME = "midiFilename";

    /**
        *   Constructor
        *   Takes and keeps a reference of the passed context  in order to access to the application assets and resources.
        *
        */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

//        return checkDB != null ? true : false;
        return false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLiteException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.


    /*
        private SQLiteDatabase myDataBase;
        public static final String KEY_ID = "id";
        public static final String MIDITABLE = "SongDB";
        public static final String ETITLE = "eTitle";
        public static final String CATEGORY = "Cat";
        public static final String SUBCATEGORY = "SubCat";
        public static final String ELYRICS = "eLyrics";
        public static final String MIDIFILENAME = "midiFilename";
         */
    public ArrayList<SongDBEntry> listSongs() {
        int songIndex;
        SongDBEntry dbEntry;
        String songTitle, filename, lyrics;
        String query = "Select * FROM " + MIDITABLE + " ORDER BY " + myContext.getString(R.string.db_title);
        ArrayList<FileUri> dbSonglist = new ArrayList<FileUri>();

        ArrayList<SongDBEntry> dbSonglist2 = new ArrayList<SongDBEntry>();

        Cursor cursor = myDataBase.rawQuery(query, null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            songIndex = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            songTitle = cursor.getString(cursor.getColumnIndex(myContext.getString(R.string.db_title)));
            filename = cursor.getString(cursor.getColumnIndex(MIDIFILENAME));
            lyrics = cursor.getString(cursor.getColumnIndex((myContext.getString(R.string.db_lyrics))));

            Uri uri = Uri.parse("file:///android_asset/MIDI/" + filename);
            FileUri file = new FileUri(uri, filename);

            dbEntry = new SongDBEntry(songIndex, songTitle, file);
            lyrics = lyrics.replaceAll("\n", " ");
            lyrics = lyrics.replaceAll("\\s+", " ");
            dbEntry.setLyrics(lyrics);

            dbSonglist.add(file);
            dbSonglist2.add(dbEntry);
        }
        return dbSonglist2;
    }

    public ArrayList<SongDBEntry> listSongsByNumber() {
        int songIndex;
        SongDBEntry dbEntry;
        String songTitle, filename;
        String query = "Select * FROM " + MIDITABLE + " ORDER BY " + KEY_ID;
        ArrayList<FileUri> dbSonglist = new ArrayList<FileUri>();

        ArrayList<SongDBEntry> dbSonglist2 = new ArrayList<SongDBEntry>();

        Cursor cursor = myDataBase.rawQuery(query, null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            songIndex = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            songTitle = cursor.getString(cursor.getColumnIndex(myContext.getString(R.string.db_title)));
            filename = cursor.getString(cursor.getColumnIndex(MIDIFILENAME));

            Uri uri = Uri.parse("file:///android_asset/MIDI/" + filename);
            FileUri file = new FileUri(uri, filename);

            dbEntry = new SongDBEntry(songIndex, songTitle, file);

            dbSonglist.add(file);
            dbSonglist2.add(dbEntry);
        }
        return dbSonglist2;
    }

    public SongDBEntry fetchSong(int songIndex) {
        SongDBEntry dbEntry;
        String songTitle, filename, songLyrics;

        String query = "Select * FROM " + MIDITABLE + " WHERE " + KEY_ID + "=" + songIndex;

        Cursor cursor = myDataBase.rawQuery(query, null);

        cursor.moveToPosition(0);

        Log.d(TAG, "fetchSong QueryCount: " + cursor.getCount());

        songTitle = cursor.getString(cursor.getColumnIndex(myContext.getString(R.string.db_title)));
        filename = cursor.getString(cursor.getColumnIndex(MIDIFILENAME));
        Uri uri = Uri.parse("file:///android_asset/MIDI/" + filename);
        FileUri file = new FileUri(uri, filename);
        songLyrics = cursor.getString(cursor.getColumnIndex(myContext.getString(R.string.db_lyrics)));

        dbEntry = new SongDBEntry(songIndex, songTitle, file);
        dbEntry.setLyrics(songLyrics);

        return dbEntry;
    }
}
