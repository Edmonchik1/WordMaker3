package com.edmon.wordmaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private GridLayout puzzleGrid;
    private GridLayout letterGrid;
    private TextView currentWordText, levelText, gemCountText;
    private String targetWord = "";
    private StringBuilder currentWord = new StringBuilder();
    private List<Character> shuffledLetters = new ArrayList<>();
    private Set<String> foundWords = new HashSet<>();
    private int currentLevel = 0;
    private final Random random = new Random();

    private int restartCount = 0;
    private final int MAX_RESTARTS = 2;
    private int gemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        puzzleGrid = findViewById(R.id.puzzleGrid);
        letterGrid = findViewById(R.id.letterGrid);
        currentWordText = findViewById(R.id.currentWord);
        levelText = findViewById(R.id.levelText);
        gemCountText = findViewById(R.id.gemCountText);

        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        gemCount = prefs.getInt("gems", 5);

        if (getIntent().hasExtra("level")) {
            currentLevel = getIntent().getIntExtra("level", 0);
        }

        Button levelSelectButton = findViewById(R.id.levelSelectButton);
        levelSelectButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LevelSelectionActivity.class);
            startActivity(intent);
        });

        Button wordLibraryButton = findViewById(R.id.wordLibraryButton);
        wordLibraryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WordLiburaryActivity.class);
            startActivity(intent);
        });

        updateGemDisplay();
        loadLevel(currentLevel, false);
    }

    private void updateGemDisplay() {
        if (gemCountText != null) {
            gemCountText.setText("Gems: " + gemCount);
        }
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        prefs.edit().putInt("gems", gemCount).apply();
    }

    private void loadLevel(int level, boolean forceNewLetters) {
        if (level != currentLevel) {
            restartCount = 0;
        }
        puzzleGrid.removeAllViews();
        letterGrid.removeAllViews();
        currentWord.setLength(0);
        currentWordText.setText("");
        foundWords.clear();

        targetWord = LevelManager.getWordForLevel(level);
        if (targetWord == null) {
            Toast.makeText(this, "No more levels!", Toast.LENGTH_LONG).show();
            return;
        }

        levelText.setText("Level: " + (level + 1));

        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        String savedLettersKey = "letters_level_" + level;

        shuffledLetters.clear();
        if (!forceNewLetters) {
            String savedLetters = prefs.getString(savedLettersKey, null);
            if (savedLetters != null) {
                for (char c : savedLetters.toCharArray()) {
                    shuffledLetters.add(c);
                }
            }
        }

        if (shuffledLetters.isEmpty()) {
            for (char c : targetWord.toCharArray()) {
                shuffledLetters.add(c);
            }
            while (shuffledLetters.size() < 6) {
                char extra = (char) ('A' + random.nextInt(26));
                if (!shuffledLetters.contains(extra))
                    shuffledLetters.add(extra);
            }
            Collections.shuffle(shuffledLetters);
            StringBuilder sb = new StringBuilder();
            for (char c : shuffledLetters) {
                sb.append(c);
            }
            prefs.edit().putString(savedLettersKey, sb.toString()).apply();
        }

        puzzleGrid.setColumnCount(targetWord.length());
        for (int i = 0; i < targetWord.length(); i++) {
            TextView tv = new TextView(this);
            tv.setText("_");
            tv.setTextSize(32);
            tv.setPadding(8, 8, 8, 8);
            tv.setId(View.generateViewId());
            puzzleGrid.addView(tv);
        }

        for (char c : shuffledLetters) {
            Button btn = new Button(this);
            btn.setText(String.valueOf(c));
            btn.setTextSize(24);
            btn.setOnClickListener(v -> {
                currentWord.append(c);
                currentWordText.setText(currentWord.toString());
            });
            letterGrid.addView(btn);
        }
    }

    private void unlockNextLevel() {
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        int unlocked = prefs.getInt("unlocked_level", 0);
        if (currentLevel + 1 > unlocked) {
            prefs.edit().putInt("unlocked_level", currentLevel + 1).apply();
        }
        Set<String> completed = prefs.getStringSet("completed_levels", new HashSet<>());
        completed.add(String.valueOf(currentLevel));
        prefs.edit().putStringSet("completed_levels", completed).apply();
    }

    public void onSubmitWord(View view) {
        String word = currentWord.toString();
        if (word.equals(targetWord)) {
            if (!foundWords.contains(word)) {
                foundWords.add(word);
                for (int i = 0; i < word.length(); i++) {
                    TextView tv = (TextView) puzzleGrid.getChildAt(i);
                    tv.setText(String.valueOf(word.charAt(i)));
                }
                Toast.makeText(this, "Correct! Level Up!", Toast.LENGTH_SHORT).show();
                unlockNextLevel();
                currentLevel++;
                gemCount++;
                updateGemDisplay();
                loadLevel(currentLevel, false);
            }
        } else {
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
        }
        currentWord.setLength(0);
        currentWordText.setText("");
    }

    public void onClearWord(View view) {
        currentWord.setLength(0);
        currentWordText.setText("");
    }

    public void onRestartLevel(View view) {
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        Set<String> completed = prefs.getStringSet("completed_levels", new HashSet<>());

        boolean isCompleted = completed.contains(String.valueOf(currentLevel));

        if (!isCompleted) {
            if (restartCount < MAX_RESTARTS) {
                restartCount++;
                loadLevel(currentLevel, true);
                Toast.makeText(this, "Restarted (" + restartCount + "/" + MAX_RESTARTS + ")", Toast.LENGTH_SHORT).show();
            } else if (gemCount > 0) {
                gemCount--;
                updateGemDisplay();
                Toast.makeText(this, "Used 1 gem to restart!", Toast.LENGTH_SHORT).show();
                loadLevel(currentLevel, true);
            } else {
                Toast.makeText(this, "No more restarts allowed and no gems!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Level already completed. Restart not allowed.", Toast.LENGTH_LONG).show();
        }
    }
}
