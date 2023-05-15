package com.smitpatel.enigmamachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Plugboard {

    private static final String TAG = "PlugBoard";
    private int[] mPlugboardMap;
    private Stack<Integer> mNextColor;
    // first int is letter, second int is color
    private Map<Integer, Integer> mPlugboardLookup;

    public Plugboard() {

        mPlugboardMap = EnigmaUtils.ENIGMA_PLUGBOARD_DEFAULT;
        mNextColor = new Stack<>();
        mPlugboardLookup = new HashMap<>();
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR13);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR12);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR11);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR10);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR9);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR8);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR7);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR6);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR5);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR4);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR3);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR2);
        mNextColor.push(EnigmaUtils.ENIGMA_PLUGBOARD_COLOR1);
    }

    public int addPair(int letterOne, int letterTwo) {
//        Log.d(TAG, ":addPair");
        mPlugboardMap[letterOne] = letterTwo;
        mPlugboardMap[letterTwo] = letterOne;
        int color = mNextColor.pop();
        mPlugboardLookup.put(letterOne, color);
        mPlugboardLookup.put(letterTwo, color);
//        Log.d(TAG, "\tmPlugboardMap: " + mPlugboardMap.toString());
//        Log.d(TAG, "\tmNextColor: " + mNextColor.toString());
//        Log.d(TAG, "\tmPlugBoardLookup: " + mPlugboardLookup.toString());
        return color;
    }

    public void removePair(int letter) {
//        Log.d(TAG, ":removePair");
        int letterTwo = mPlugboardMap[letter];
        mPlugboardMap[letter] = -1;
        mPlugboardMap[letterTwo] = -1;
        mNextColor.push(mPlugboardLookup.get(letter));
        mPlugboardLookup.remove(letter);
        mPlugboardLookup.remove(letterTwo);
//        Log.d(TAG, "\tmPlugboardMap: " + mPlugboardMap.toString());
//        Log.d(TAG, "\tmNextColor: " + mNextColor.toString());
//        Log.d(TAG, "\tmPlugBoardLookup: " + mPlugboardLookup.toString());
    }

    public int getPair(int letter) {
//        Log.d(TAG, ":getPair");
        return hasPair(letter) ? mPlugboardMap[letter] : letter;
    }

    public boolean hasPair(int letter) {
        return mPlugboardMap[letter] != -1;
    }

    public int getTopColor() {
//        Log.d(TAG, ":getTopColor");
//        Log.d(TAG, "\tmNextColor: " + mNextColor.toString());
        return mNextColor.peek();
    }

    public int[] getPlugboardMap() {
        return mPlugboardMap;
    }

    public Map<Integer, Integer> getColorMap() {
        return mPlugboardLookup;
    }
}
