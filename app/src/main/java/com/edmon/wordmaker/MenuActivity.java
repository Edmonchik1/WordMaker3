package com.edmon.wordmaker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button mainButton = findViewById(R.id.buttonMain);
        Button levelSelectButton = findViewById(R.id.buttonLevelSelect);
        Button wordLibraryButton = findViewById(R.id.buttonWordLibrary);

        mainButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        levelSelectButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, LevelSelectionActivity.class);
            startActivity(intent);
        });

        wordLibraryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, WordLiburaryActivity.class);
            startActivity(intent);
        });
    }
}
