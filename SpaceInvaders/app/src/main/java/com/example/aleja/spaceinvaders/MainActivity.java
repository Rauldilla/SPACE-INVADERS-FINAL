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

//        final Button adultButton = (Button) this.findViewById(R.id.adult);
//        adultButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d("debug", "selected adult");
//                Intent intent = new Intent(MainActivity.this, SpaceInvadersActivity.class);
//                intent.putExtra("adult", true);
//                startActivity(intent);
//            }
//        });
//
//        final Button babyButton = (Button) this.findViewById(R.id.baby);
//        babyButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.d("debug", "selected baby");
//                Intent intent = new Intent(MainActivity.this, SpaceInvadersActivity.class);
//                intent.putExtra("adult", false);
//                startActivity(intent);
//            }
//        });

        final EditText text = (EditText) this.findViewById(R.id.age);

        final Button confirm = (Button) this.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = text.getText().toString();
                Log.d("debug", str);
                Intent intent = new Intent(MainActivity.this, SpaceInvaders.class);
                intent.putExtra("adult", Integer.parseInt(str) >= 16);
                startActivity(intent);
            }
        });
    }
}
