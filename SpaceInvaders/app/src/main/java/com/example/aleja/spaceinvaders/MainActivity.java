package com.example.aleja.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_age);

        final EditText text = (EditText) this.findViewById(R.id.age);

        final Button confirm = (Button) this.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = text.getText().toString();
                Log.d("debug", str);
                Intent intent = new Intent(MainActivity.this, SpaceInvaders.class);
                intent.putExtra("adult", Integer.parseInt(str) > 13);
                startActivity(intent);
            }
        });
    }
}
