package com.example.aleja.spaceinvaders;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class RankingActivity extends Activity {
    private ScoreDdHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_view);

        final ImageView imagenFinal = this.findViewById(R.id.imageView);
        final Button back = this.findViewById(R.id.back);

        imagenFinal.setImageResource(R.drawable.ranking);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RankingActivity.this.finish();
            }
        });
        this.helper = new ScoreDdHelper(this);
        final ListView listView = this.findViewById(R.id.ranking);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.ranking, this.helper.findTop10Ranking()));
    }
}
