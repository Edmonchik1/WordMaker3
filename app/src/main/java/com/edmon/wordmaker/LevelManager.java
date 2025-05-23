package com.edmon.wordmaker;

import java.util.Arrays;
import java.util.List;

public class LevelManager {

    private static final List<String> LEVEL_WORDS = Arrays.asList(
            "PACK", "SICK", "LICK", "KISS", "FISH", "SHIP", "TRAP", "CLAP", "PINK", "CARD",
            "DARK", "MASK", "JUMP", "CAMP", "ROAD", "BIKE", "TREE", "FROG", "LAMP", "MOON",
            "RING", "CAVE", "KING", "BOOK", "HOOK", "LOOK", "COOK", "TIME", "FIRE", "WIND",
            "BIRD", "DUCK", "FORK", "SINK", "SOAP", "NOTE", "TONE", "DUST", "GOLD", "FIND",
            "FAST", "SLOW", "MOVE", "WALK", "TALK", "DROP", "STOP", "PLAY", "GLOW", "SNOW"
    );

    public static String getWordForLevel(int level) {
        if (level < LEVEL_WORDS.size()) {
            return LEVEL_WORDS.get(level);
        } else {
            return null;
        }
    }

    public static int getTotalLevels() {
        return LEVEL_WORDS.size();
    }
}
