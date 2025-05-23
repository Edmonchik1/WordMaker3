package com.edmon.wordmaker;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WordLiburaryActivity extends AppCompatActivity {

    private ListView wordListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_liburary);

        wordListView = findViewById(R.id.wordListView);

        Set<String> savedWords = getSharedPreferences("game_prefs", MODE_PRIVATE)
                .getStringSet("found_words", new HashSet<>());

        wordList = new ArrayList<>(savedWords);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList);
        wordListView.setAdapter(adapter);
    }
}
