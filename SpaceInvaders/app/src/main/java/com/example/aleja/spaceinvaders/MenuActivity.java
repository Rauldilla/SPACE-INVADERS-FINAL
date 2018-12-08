package com.example.aleja.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity {
    private ScoreDdHelper helper;
    private ImageView photoView;
    private long lastRecord;
    private ScoreDdHelper.ScoreRecordEntry lastItem;

    MediaPlayer mediaPlayerVictoria;
    MediaPlayer mediaPlayerDerrota;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new ScoreDdHelper(this);

        mediaPlayerVictoria = MediaPlayer.create(this, R.raw.mvictoria);
        mediaPlayerVictoria.setLooping(true);
        mediaPlayerDerrota = MediaPlayer.create(this, R.raw.mderrota);
        mediaPlayerDerrota.setLooping(true);

        setContentView(R.layout.menu);

        final ImageView imagenFinal = this.findViewById(R.id.imagen);

        Bundle extras = getIntent().getExtras();
        final boolean win = extras.getBoolean(getResources().getString(R.string.victory));
        if (win) {
            imagenFinal.setImageResource(R.drawable.win);
            mediaPlayerVictoria.start();
        } else {
            imagenFinal.setImageResource(R.drawable.game_over);
            mediaPlayerDerrota.start();
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
                mediaPlayerDerrota.pause();
                mediaPlayerVictoria.pause();
                MenuActivity.this.finish();
            }
        });

        this.lastItem = new ScoreDdHelper.ScoreRecordEntry(name, score);
        this.lastRecord = this.helper.insertNewRecord(lastItem);

        final Button viewRanking = this.findViewById(R.id.view_ranking);
        viewRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });

        final Button redo = this.findViewById(R.id.redo);
        redo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayerDerrota.pause();
                mediaPlayerVictoria.pause();

                Intent intent = new Intent(MenuActivity.this, SpaceInvaders.class);
                Bundle extras = getIntent().getExtras();

                final boolean isAdult = extras.getBoolean("adult");
                final String name = extras.getString(getResources().getString(R.string.name));
                final boolean rebote = extras.getBoolean("rebote");
                intent.putExtra("adult", isAdult);
                intent.putExtra(getResources().getString(R.string.name), name);
                intent.putExtra("rebote", rebote);
                startActivity(intent);
            }
        });
        this.photoView = this.findViewById(R.id.photo);
        dispatchTakePictureIntent();
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            final Bitmap resized = Bitmap.createScaledBitmap(imageBitmap, 120, 160, false);
            photoView.setImageBitmap(resized);

            this.lastItem.setPhoto(Utils.encodeToBase64(resized));
            this.helper.updatePhoto(this.lastRecord, this.lastItem);
        }
    }
}
