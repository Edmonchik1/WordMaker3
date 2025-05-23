package com.edmon.wordmaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LevelSelectionActivity extends AppCompatActivity {

    private static final int TOTAL_LEVELS = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        LinearLayout levelsLayout = findViewById(R.id.levelsLayout);
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        int unlockedLevel = prefs.getInt("unlocked_level", 0);

        for (int i = 0; i < TOTAL_LEVELS; i++) {
            Button levelButton = new Button(this);
            levelButton.setText("Level " + (i + 1));

            if (i <= unlockedLevel) {
                int finalI = i;
                levelButton.setOnClickListener(v -> {
                    Intent intent = new Intent(LevelSelectionActivity.this, MainActivity.class);
                    intent.putExtra("level", finalI);
                    startActivity(intent);
                });
            } else {
                levelButton.setEnabled(false);
            }

            levelsLayout.addView(levelButton);
        }
    }
}