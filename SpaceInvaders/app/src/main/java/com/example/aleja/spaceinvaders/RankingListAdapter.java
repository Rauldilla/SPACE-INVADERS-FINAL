package com.example.aleja.spaceinvaders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RankingListAdapter extends ArrayAdapter<ScoreDdHelper.ScoreRecordEntry> {

    private List<ScoreDdHelper.ScoreRecordEntry> entries;
    private Context context;

    public RankingListAdapter(Context context, List<ScoreDdHelper.ScoreRecordEntry> entries) {
        super(context, -1, entries);
        this.entries = entries;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.ranking, parent, false);

            ImageView photoView = view.findViewById(R.id.ItemPhoto);
            TextView nameLabel = view.findViewById(R.id.ItemName);
            TextView scoreLabel = view.findViewById(R.id.ItemScore);

            nameLabel.setText("Nombre: " + this.entries.get(position).getName());
            scoreLabel.setText("Puntuación: " + this.entries.get(position).getScore());
            if (this.entries.get(position).getPhoto() != null) {
                photoView.setImageBitmap(Utils.decodeFromBase64(this.entries.get(position).getPhoto()));
            }
        }
        return view;
    }
}
