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
        private String photo;

        public ScoreRecordEntry(String name, Integer score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        @Override
        public String toString() {
            return "Nombre:  " + name + '\t' + "   Puntos:  " + score;
        }
    }

    public static class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "score";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SCORE = "score";
        public static final String COLUMN_PHOTO_BASE64 = "photo";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Score.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ScoreEntry.TABLE_NAME + " (" +
                    ScoreEntry._ID + " INTEGER PRIMARY KEY," +
                    ScoreEntry.COLUMN_NAME_NAME + " TEXT," +
                    ScoreEntry.COLUMN_NAME_SCORE + " INTEGER," +
                    ScoreEntry.COLUMN_PHOTO_BASE64 + " TEXT)";

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

    public long insertNewRecord(ScoreRecordEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScoreDdHelper.ScoreEntry.COLUMN_NAME_NAME, entry.name);
        values.put(ScoreDdHelper.ScoreEntry.COLUMN_NAME_SCORE, entry.score);
        long newRowId = db.insert(ScoreDdHelper.ScoreEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public void updatePhoto(long id, ScoreRecordEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScoreDdHelper.ScoreEntry.COLUMN_NAME_NAME, entry.name);
        values.put(ScoreDdHelper.ScoreEntry.COLUMN_NAME_SCORE, entry.score);
        values.put(ScoreEntry.COLUMN_PHOTO_BASE64, entry.photo);
        db.update(ScoreDdHelper.ScoreEntry.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<ScoreRecordEntry> findTop10Ranking() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ScoreDdHelper.ScoreEntry.COLUMN_NAME_NAME,
                ScoreDdHelper.ScoreEntry.COLUMN_NAME_SCORE,
                ScoreDdHelper.ScoreEntry.COLUMN_PHOTO_BASE64
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
        int photoIndex = cursor.getColumnIndex(ScoreDdHelper.ScoreEntry.COLUMN_PHOTO_BASE64);
        while (cursor.moveToNext()) {
            final ScoreRecordEntry item = new ScoreRecordEntry(cursor.getString(nameIndex), cursor.getInt(scoreIndex));
            item.setPhoto(cursor.getString(photoIndex));
            items.add(item);
        }
        cursor.close();
        db.close();
        return items;
    }
}
