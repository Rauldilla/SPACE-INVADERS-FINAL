package com.example.aleja.spaceinvaders;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends Activity {
    private ScoreDdHelper helper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new ScoreDdHelper(this);

        setContentView(R.layout.menu);

        final ImageView imagenFinal = this.findViewById(R.id.imagen);

        Bundle extras = getIntent().getExtras();
        final boolean win = extras.getBoolean(getResources().getString(R.string.victory));
        if (win) {
            imagenFinal.setImageResource(R.drawable.win);
        } else {
            imagenFinal.setImageResource(R.drawable.game_over);
        }

        final TextView scoreLabel = this.findViewById(R.id.score);
        final int score = extras.getInt(getResources().getString(R.string.score));
        scoreLabel.setText(String.valueOf(score));

        final String name = extras.getString(getResources().getString(R.string.name));
        final TextView nameLabel = this.findViewById(R.id.name);
        nameLabel.setText(name);
        Log.d("debug", name);

        final Button back = this.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MenuActivity.this.finish();
            }
        });

        SQLiteDatabase db = this.helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScoreDdHelper.ScoreEntry.COLUMN_NAME_NAME, name);
        values.put(ScoreDdHelper.ScoreEntry.COLUMN_NAME_SCORE, score);
        long newRowId = db.insert(ScoreDdHelper.ScoreEntry.TABLE_NAME, null, values);

        db = this.helper.getReadableDatabase();
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

        List<Entry> items = new ArrayList<>();
        int nameIndex = cursor.getColumnIndex(ScoreDdHelper.ScoreEntry.COLUMN_NAME_NAME);
        int scoreIndex = cursor.getColumnIndex(ScoreDdHelper.ScoreEntry.COLUMN_NAME_SCORE);
        while (cursor.moveToNext()) {
            items.add(new Entry(cursor.getString(nameIndex), cursor.getInt(scoreIndex)));
        }
        cursor.close();

        final ListView listView = this.findViewById(R.id.ranking);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.ranking, items));
    }

    private static class Entry {
        private String name;
        private Integer score;

        private Entry(String name, Integer score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return "User:'" + name + '\t' + "Score:" + score;
        }
    }
}
