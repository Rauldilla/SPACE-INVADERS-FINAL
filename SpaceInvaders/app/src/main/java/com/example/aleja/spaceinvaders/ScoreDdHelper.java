package com.example.aleja.spaceinvaders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class ScoreDdHelper extends SQLiteOpenHelper {
    public static class ScoreRecordEntry {
        private String name;
        private Integer score;

        private ScoreRecordEntry(String name, Integer score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return "User:'" + name + '\t' + "Score:" + score;
        }
    }

    public static class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "score";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SCORE = "score";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Score.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ScoreEntry.TABLE_NAME + " (" +
                    ScoreEntry._ID + " INTEGER PRIMARY KEY," +
                    ScoreEntry.COLUMN_NAME_NAME + " TEXT," +
                    ScoreEntry.COLUMN_NAME_SCORE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME;

    public ScoreDdHelper(Context context) {
        super(context, ScoreEntry.TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertNewRecord(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScoreDdHelper.ScoreEntry.COLUMN_NAME_NAME, name);
        values.put(ScoreDdHelper.ScoreEntry.COLUMN_NAME_SCORE, score);
        long newRowId = db.insert(ScoreDdHelper.ScoreEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public List<ScoreRecordEntry> findTop10Ranking() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ScoreDdHelper.ScoreEntry.COLUMN_NAME_NAME,
                ScoreDdHelper.ScoreEntry.COLUMN_NAME_SCORE
        };

        Cursor cursor = db.query(
                ScoreDdHelper.ScoreEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                ScoreDdHelper.ScoreEntry.COLUMN_NAME_SCORE + " DESC",
                "10"
        );

        List<ScoreRecordEntry> items = new ArrayList<>();
        int nameIndex = cursor.getColumnIndex(ScoreDdHelper.ScoreEntry.COLUMN_NAME_NAME);
        int scoreIndex = cursor.getColumnIndex(ScoreDdHelper.ScoreEntry.COLUMN_NAME_SCORE);
        while (cursor.moveToNext()) {
            items.add(new ScoreRecordEntry(cursor.getString(nameIndex), cursor.getInt(scoreIndex)));
        }
        cursor.close();
        db.close();
        return items;
    }
}
