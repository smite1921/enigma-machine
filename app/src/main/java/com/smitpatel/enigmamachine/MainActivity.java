package com.smitpatel.enigmamachine;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import com.shawnlin.numberpicker.NumberPicker;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, NumberPicker.OnValueChangeListener {

    private static final String TAG = "MainActivity";
    private NumberPicker mPicker1, mPicker2,mPicker3;
    private Rotor mRotor1, mRotor2, mRotor3;
    private Reflector mReflector;
    private Plugboard mPlugboard;
    private Button[] mButtons;
    private Button[] mLamps;
    private TextView mRaw, mCoded;
    private HorizontalScrollView mScrollview;
    private List<Button> mClickedLamps;
    private List<Button> mClickedButtons;
    private SoundEffects mSounds;

    /**
     * Called when application is starting.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enigma_main);

        // Setup methods
        mSounds = SoundEffects.getInstance(this);
        setupDefaultEnigma();
        setupViews();

        // mClickedButtons is stack to represent what buttons have been clicked
        mClickedLamps = new ArrayList<>();
        mClickedButtons = new ArrayList<>();

        // Setup listeners
        Button btnSpace = findViewById(R.id.spacebar);
        Button btnDel = findViewById(R.id.key_delete);
        View powerSwitch = findViewById(R.id.power_switch);

        NumberPicker[] pickers = new NumberPicker[]{mPicker1,mPicker2,mPicker3};
        for (NumberPicker picker: pickers) picker.setOnValueChangedListener(this);
        for (Button mButton : mButtons) mButton.setOnTouchListener(this);
        btnSpace.setOnTouchListener(this);
        btnDel.setOnTouchListener(this);
        powerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSounds.playSound(EnigmaUtils.ENIGMA_SOUND_DEFAULT);
                SettingsDialogFragment dialog = new SettingsDialogFragment(getApplicationContext(),mRotor1, mRotor2, mRotor3, mReflector, mPlugboard);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    /**
     * Called when application is closing.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSounds.shutDown();
    }

    /**
     * This is callback method when buttons are pressed.
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        StringBuilder log = new StringBuilder()
                .append("(")
                .append(v.getId())
                .append(", ")
                .append(event.getAction())
                .append(")");
        Log.d(TAG, ": onTouch - (view, event) = " + log);

        switch(event.getAction()) {
            // Button released
            case MotionEvent.ACTION_UP:
                Button releasedButton = (Button)v;
                // If button pressed was letter, we also have to turn off lamp
                if ((releasedButton.getId() != R.id.spacebar)
                        && (releasedButton.getId() != R.id.key_delete)){
                    mClickedLamps.remove(0).setPressed(false);
                }
                mClickedButtons.remove(0).setPressed(false);
                return true;

            // Button pressed
            case MotionEvent.ACTION_DOWN:
                Button pressedButton = (Button)v;
                pressedButton.setPressed(true);
                mClickedButtons.add(pressedButton);

                if (pressedButton.getId() == R.id.spacebar) {
                    mSounds.playSound(EnigmaUtils.ENIGMA_SOUND_SPACE);
                    mRaw.append(" ");
                    mCoded.append(" ");
                }
                else if (pressedButton.getId() == R.id.key_delete) {
                    mSounds.playSound(EnigmaUtils.ENIGMA_SOUND_DELETE);
                    CharSequence rawText = mRaw.getText();
                    CharSequence codeText = mCoded.getText();
                    if ((rawText.length()>=1) && (codeText.length()>=1)){
                        char lastChar = rawText.charAt(rawText.length()-1);
                        if (lastChar != ' ') reverseEnigma();
                        mRaw.setText(rawText.subSequence(0,rawText.length()-1));
                        mCoded.setText(codeText.subSequence(0, codeText.length()-1));
                    }
                }
                else {
                    mSounds.playSound(EnigmaUtils.ENIGMA_SOUND_KEY);
                    int inputNumber = letterToNumber(pressedButton.getText().charAt(0));
                    int outputNumber = inputEnigma(inputNumber);
                    Log.d(TAG, "ouptut: " + outputNumber);
                    mLamps[outputNumber].setPressed(true);
                    mClickedLamps.add(mLamps[outputNumber]);
                    mRaw.append(numberToLetter(inputNumber));
                    mCoded.append(numberToLetter(outputNumber));
                }
                // Scroll to end of textViews
                mScrollview.post(new Runnable() {
                    @Override
                    public void run() {
                    mScrollview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                });
                return true;
            default:
                return false;
        }
    }

    /**
     * This is callback method for when values from when number picker values are changed.
     * @param picker
     * @param oldVal
     * @param newVal
     */
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, final int newVal) {
        mSounds.playSound(EnigmaUtils.ENIGMA_SOUND_ROTOR);
        StringBuilder log = new StringBuilder()
                .append("(")
                .append(oldVal)
                .append(", ")
                .append(newVal)
                .append(")");
        Log.d(TAG, ": onValueChange - (oldValue, newValue) = " + log);
        switch (picker.getId()) {
            case R.id.rotor_1:
                mRotor1.setPosition(newVal - 1);
                break;
            case R.id.rotor_2:
                mRotor2.setPosition(newVal - 1);
                break;
            case R.id.rotor_3:
                mRotor3.setPosition(newVal - 1);
                break;
        }
    }

    /**
     * Returns position of letter in alphabet.
     * @param letter
     * @return
     */
    private int letterToNumber(char letter) {
        return (int)letter - 65;
    }

    /**
     * Returns String letter for the position of a letter.
     * @param number
     * @return
     */
    private String numberToLetter(int number) {
        return Character.toString((char)(number + 65));
    }

    /**
     * Reverse the state of the enigma by one input. Made for delete key
     */
    private void reverseEnigma() {
        // Case 1: Prev Middle Rotor Position at Notch - All three rotors turn
        if (mRotor2.getTurnover() == (mRotor2.getPosition()-1)) {
            mRotor3.reverseShift();
            mRotor2.reverseShift();
            mRotor1.reverseShift();
            mPicker1.setValue(mPicker1.getValue()-1);
            mPicker2.setValue(mPicker2.getValue()-1);
            mPicker3.setValue(mPicker3.getValue()-1);
        }
        // Case 2: Prev Right Rotor Position at Notch - Right & Middle rotors turn
        else if (mRotor3.getTurnover() == (mRotor3.getPosition()-1)) {
            mRotor2.reverseShift();
            mRotor3.reverseShift();
            mPicker2.setValue(mPicker2.getValue()-1);
            mPicker3.setValue(mPicker3.getValue()-1);

        }
        // Case 3: No Notch - Just right rotor
        else {
            mRotor3.reverseShift();
            mPicker3.setValue(mPicker3.getValue()-1);
        }
    }

    /**
     * Takes in letter input, and returns the letter Enigma Machine would output.
     * @param inputLetter
     * @return
     */
    private int inputEnigma(int inputLetter) {

        // First do rotor shifts.

        // Case 1: Middle Rotor at Notch - All three rotors turn
        if (mRotor2.getTurnover() == mRotor2.getPosition()) {
//            Log.d(TAG,"All Rotors shifted");
            mRotor3.shift();
            mRotor2.shift();
            mRotor1.shift();
            mPicker1.setValue(mPicker1.getValue()+1);
            mPicker2.setValue(mPicker2.getValue()+1);
            mPicker3.setValue(mPicker3.getValue()+1);
        }
        // Case 2: Right Rotor at Notch - Right & Middle rotors turn
        else if (mRotor3.getTurnover() == mRotor3.getPosition()) {
//            Log.d(TAG,"Right & Middle Rotor shifted");
            mRotor2.shift();
            mRotor3.shift();
            mPicker2.setValue(mPicker2.getValue()+1);
            mPicker3.setValue(mPicker3.getValue()+1);

        }
        // Case 3: No Notch - Just right rotor
        else {
//            Log.d(TAG,"Right Rotor shifted");
            mRotor3.shift();
            mPicker3.setValue(mPicker3.getValue()+1);
        }

        // Forward direction
        int pbfOutput = mPlugboard.getPair(inputLetter);
        int r3fOutput = mRotor3.mapping(pbfOutput, true);
//        Log.d(TAG,"R3F: " + r3fOutput);
        int r2fOutput = mRotor2.mapping(r3fOutput, true);
//        Log.d(TAG, "R2F: " + r2fOutput);
        int r1fOutput = mRotor1.mapping(r2fOutput, true);
//        Log.d(TAG,"R1F: " + r1fOutput);

        int reflectorOutput = mReflector.input(r1fOutput);
//        Log.d(TAG,"RE: " + reflectorOutput);

        // Reverse direction
        int r1rOutput = mRotor1.mapping(reflectorOutput, false);
//        Log.d(TAG,"R1R: " + r1rOutput);
        int r2rOutput = mRotor2.mapping(r1rOutput, false);
//        Log.d(TAG,"R2R: " + r2rOutput);
        int r3rOutput = mRotor3.mapping(r2rOutput, false);
//        Log.d(TAG,"R3R: " + r3rOutput);
        int pbrOutput = mPlugboard.getPair(r3rOutput);
//        Log.d(TAG, "output: " + pbrOutput);
        return pbrOutput;
    }

    /**
     * Initialization method to setup default values of Enigma Machine
     */
    private void setupDefaultEnigma(){
        mRotor1 = new Rotor();
        mRotor2 = new Rotor();
        mRotor3 = new Rotor();
        mReflector = new Reflector();
        mPlugboard = new Plugboard();

        // Set default values
        mRotor1.setPosition(0);
        mRotor1.setTurnover(EnigmaUtils.ENIGMA_ROTOR_1_TURNOVER);
        mRotor1.setForwardMap(EnigmaUtils.ENIGMA_1_ROTOR_1F);
        mRotor1.setReverseMap(EnigmaUtils.ENIGMA_1_ROTOR_1R);
        mRotor1.setRing(0);
        mRotor1.setIdentifier(EnigmaUtils.ENIGMA_ROTOR_OPTIONS[0]);
        mRotor2.setPosition(0);
        mRotor2.setTurnover(EnigmaUtils.ENIGMA_ROTOR_2_TURNOVER);
        mRotor2.setForwardMap(EnigmaUtils.ENIGMA_1_ROTOR_2F);
        mRotor2.setReverseMap(EnigmaUtils.ENIGMA_1_ROTOR_2R);
        mRotor2.setRing(0);
        mRotor2.setIdentifier(EnigmaUtils.ENIGMA_ROTOR_OPTIONS[1]);
        mRotor3.setPosition(0);
        mRotor3.setTurnover(EnigmaUtils.ENIGMA_ROTOR_3_TURNOVER);
        mRotor3.setForwardMap(EnigmaUtils.ENIGMA_1_ROTOR_3F);
        mRotor3.setReverseMap(EnigmaUtils.ENIGMA_1_ROTOR_3R);
        mRotor3.setRing(0);
        mRotor3.setIdentifier(EnigmaUtils.ENIGMA_ROTOR_OPTIONS[2]);
        mReflector.setReflectorMap(EnigmaUtils.ENIGMA_REFLECTOR_B);
        mReflector.setIdentifer(EnigmaUtils.ENIGMA_REFLECTOR_OPTIONS[1]);
    }

    /**
     * Initialization method to setup views
     */
    private void setupViews() {
        // Setup text views at bottom of screen
        mRaw = findViewById(R.id.text_raw);
        mRaw.setMovementMethod(new ScrollingMovementMethod());
        mCoded = findViewById(R.id.text_code);
        mScrollview = findViewById(R.id.scrollview);

        // Setup number pickers
        mPicker1 = findViewById(R.id.rotor_1);
        mPicker2 = findViewById(R.id.rotor_2);
        mPicker3 = findViewById(R.id.rotor_3);
        mPicker1.setMinValue(1);
        mPicker1.setMaxValue(EnigmaUtils.ENIGMA_ROTOR_LETTERS.length);
        mPicker1.setDisplayedValues(EnigmaUtils.ENIGMA_ROTOR_LETTERS);
        mPicker2.setMinValue(1);
        mPicker2.setMaxValue(EnigmaUtils.ENIGMA_ROTOR_LETTERS.length);
        mPicker2.setDisplayedValues(EnigmaUtils.ENIGMA_ROTOR_LETTERS);
        mPicker3.setMinValue(1);
        mPicker3.setMaxValue(EnigmaUtils.ENIGMA_ROTOR_LETTERS.length);
        mPicker3.setDisplayedValues(EnigmaUtils.ENIGMA_ROTOR_LETTERS);

        // Setup button views
        Button btnA = findViewById(R.id.button_a);
        Button btnB = findViewById(R.id.button_b);
        Button btnC = findViewById(R.id.button_c);
        Button btnD = findViewById(R.id.button_d);
        Button btnE = findViewById(R.id.button_e);
        Button btnF = findViewById(R.id.button_f);
        Button btnG = findViewById(R.id.button_g);
        Button btnH = findViewById(R.id.button_h);
        Button btnI = findViewById(R.id.button_i);
        Button btnJ = findViewById(R.id.button_j);
        Button btnK = findViewById(R.id.button_k);
        Button btnL = findViewById(R.id.button_l);
        Button btnN = findViewById(R.id.button_n);
        Button btnM = findViewById(R.id.button_m);
        Button btnO = findViewById(R.id.button_o);
        Button btnP = findViewById(R.id.button_p);
        Button btnQ = findViewById(R.id.button_q);
        Button btnR = findViewById(R.id.button_r);
        Button btnS = findViewById(R.id.button_s);
        Button btnT = findViewById(R.id.button_t);
        Button btnU = findViewById(R.id.button_u);
        Button btnV = findViewById(R.id.button_v);
        Button btnW = findViewById(R.id.button_w);
        Button btnX = findViewById(R.id.button_x);
        Button btnY = findViewById(R.id.button_y);
        Button btnZ = findViewById(R.id.button_z);
        mButtons = new Button[]{btnA, btnB, btnC, btnD, btnE, btnF, btnG, btnH, btnI, btnJ, btnK,
                btnL, btnM, btnN, btnO, btnP, btnQ, btnR, btnS, btnT, btnU, btnV, btnW, btnX, btnY,
                btnZ};

        // Setup lamp views
        Button lampA = findViewById(R.id.lamp_a);
        Button lampB = findViewById(R.id.lamp_b);
        Button lampC = findViewById(R.id.lamp_c);
        Button lampD = findViewById(R.id.lamp_d);
        Button lampE = findViewById(R.id.lamp_e);
        Button lampF = findViewById(R.id.lamp_f);
        Button lampG = findViewById(R.id.lamp_g);
        Button lampH = findViewById(R.id.lamp_h);
        Button lampI = findViewById(R.id.lamp_i);
        Button lampJ = findViewById(R.id.lamp_j);
        Button lampK = findViewById(R.id.lamp_k);
        Button lampL = findViewById(R.id.lamp_l);
        Button lampN = findViewById(R.id.lamp_n);
        Button lampM = findViewById(R.id.lamp_m);
        Button lampO = findViewById(R.id.lamp_o);
        Button lampP = findViewById(R.id.lamp_p);
        Button lampQ = findViewById(R.id.lamp_q);
        Button lampR = findViewById(R.id.lamp_r);
        Button lampS = findViewById(R.id.lamp_s);
        Button lampT = findViewById(R.id.lamp_t);
        Button lampU = findViewById(R.id.lamp_u);
        Button lampV = findViewById(R.id.lamp_v);
        Button lampW = findViewById(R.id.lamp_w);
        Button lampX = findViewById(R.id.lamp_x);
        Button lampY = findViewById(R.id.lamp_y);
        Button lampZ = findViewById(R.id.lamp_z);
        mLamps = new Button[]{lampA, lampB, lampC, lampD, lampE, lampF, lampG, lampH, lampI,
                lampJ, lampK, lampL, lampM, lampN, lampO, lampP, lampQ, lampR, lampS, lampT, lampU,
                lampV, lampW, lampX, lampY, lampZ};
    }
}


