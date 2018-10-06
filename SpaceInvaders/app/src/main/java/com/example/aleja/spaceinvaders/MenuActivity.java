package com.example.aleja.spaceinvaders;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        final TextView endingLabel = this.findViewById(R.id.ending);
        final ImageView imagenFinal = this.findViewById(R.id.imagen);

        Bundle extras = getIntent().getExtras();
        final boolean win = extras.getBoolean(getResources().getString(R.string.victory));
        if (win) {
            endingLabel.setText(R.string.victory);
            imagenFinal.setImageResource(R.drawable.win);
        } else {
            endingLabel.setText(R.string.fail);
            imagenFinal.setImageResource(R.drawable.game_over);
        }

        final TextView scoreLabel = this.findViewById(R.id.score);
        final int score = extras.getInt(getResources().getString(R.string.score));
        scoreLabel.setText(String.valueOf(score));

        final Button back = this.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MenuActivity.this.finish();
            }
        });
    }
}
