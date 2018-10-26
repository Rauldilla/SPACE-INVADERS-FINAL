package com.example.aleja.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro);

        final EditText nameText = this.findViewById(R.id.name);

        final EditText ageText = this.findViewById(R.id.age);

        final Button confirm = this.findViewById(R.id.confirm);

        final Switch switchReb = this.findViewById(R.id.switch1);

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String age = ageText.getText().toString();
                String name = nameText.getText().toString();
                boolean rebo = switchReb.isChecked();
                Log.d("debug", age);
                Log.d("debug", name);
                Intent intent = new Intent(MainActivity.this, SpaceInvaders.class);
                intent.putExtra("adult", Integer.parseInt(age) > 13);
                intent.putExtra(getResources().getString(R.string.name), name);
                intent.putExtra("rebote", rebo);
                startActivity(intent);
            }
        });

    }
}
