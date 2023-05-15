package com.smitpatel.enigmamachine;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Arrays;
import java.util.Map;

public class SettingsDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "SettingsDialogFragment";
    private Rotor mRotor1, mRotor2, mRotor3;
    private Reflector mReflector;
    private Plugboard mPlugboard;
    private RadioGroup mReflectorRadiogroup;
    private Spinner mRotorSpinner1, mRotorSpinner2, mRotorSpinner3,mRingSpinner1,mRingSpinner2,mRingSpinner3;
    private SoundEffects mSounds;
    private Button[] mPlugArray;
    private Bundle mInitialValues;

    // These 2 are used for plugboard settings
    private boolean mFlag;
    private Button mLastPlug;

    /**
     * Constructor for Settings Prompt
     * @param r1
     * @param r2
     * @param r3
     * @param re
     * @param pb
     */
    public SettingsDialogFragment(Rotor r1, Rotor r2, Rotor r3,
                                  Reflector re, Plugboard pb) {
        Log.d(TAG, ": SettingsDialogFragment");
        // Enigma parts
        mRotor1 = r1;
        mRotor2 = r2;
        mRotor3 = r3;
        mReflector = re;
        mPlugboard = pb;
        mFlag = false;

        mSounds = SoundEffects.getInstance(getContext());
        storeInitialValues();
    }

    /**
     * Fragment Lifecycle - onCreateDialog
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, ": onCreateDialog");
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /**
     * Fragment LifeCycle - onResume
     * @return
     */
    @Override
    public void onResume() {
        Log.d(TAG, ": onResume");
        // Resize the dialog to full screen.
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        super.onResume();
    }

    /**
     * Fragment LifeCycle - onCreateView
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enigma_settings, container, false);
        Log.d(TAG, ": onCreateView");
        // Initial setup
        setupViews(view);
        setupInitialValues();

        // Setup listeners
        mReflectorRadiogroup.setOnCheckedChangeListener(this);

        for (Button plug : mPlugArray) plug.setOnClickListener(this);

        Spinner[] slots = {mRotorSpinner1, mRotorSpinner2, mRotorSpinner3};
        for (Spinner spinner: slots) spinner.setOnItemSelectedListener(this);

        Spinner[] rings = {mRingSpinner1, mRingSpinner2, mRingSpinner3};
        for (Spinner ring: rings) ring.setOnItemSelectedListener(this);

        SwitchMaterial muteSwitch = view.findViewById(R.id.mute_switch);
        muteSwitch.setChecked(!mSounds.isMute());
        muteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSounds.setMute(!isChecked);
            }
        });

        Button cancel = view.findViewById(R.id.close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ": onClick - closing dialog");
                if (settingsChanged()) {
                    mSounds.playSound(EnigmaUtils.ENIGMA_SOUND_CHANGES);
                    TextView textRaw = getActivity().findViewById(R.id.text_raw);
                    TextView textCode = getActivity().findViewById(R.id.text_code);
                    textRaw.setText("");
                    textCode.setText("");
                }
                getDialog().dismiss();
            }
        });

        Button instructions = view.findViewById(R.id.instructions);
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/smite1921/enigma_machine/blob/master/README.md#guide");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * Callback for when reflector options.
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        StringBuilder log = new StringBuilder()
                .append("(")
                .append(checkedId)
                .append(")");
        Log.d(TAG, ":onCheckedChanged - (checkedID) = " + log);

        switch (checkedId) {
            case R.id.reflector_ukwa:
                mReflector.setIdentifer(EnigmaUtils.ENIGMA_REFLECTOR_OPTIONS[0]);
                mReflector.setReflectorMap(EnigmaUtils.ENIGMA_REFLECTOR_A);
                break;
            case R.id.reflector_ukwb:
                mReflector.setIdentifer(EnigmaUtils.ENIGMA_REFLECTOR_OPTIONS[1]);
                mReflector.setReflectorMap(EnigmaUtils.ENIGMA_REFLECTOR_B);
                break;
            case R.id.reflector_ukwc:
                mReflector.setIdentifer(EnigmaUtils.ENIGMA_REFLECTOR_OPTIONS[2]);
                mReflector.setReflectorMap(EnigmaUtils.ENIGMA_REFLECTOR_C);
                break;
        }
    }

    /**
     * Callback for when rotor or ring options are selected.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        StringBuilder log = new StringBuilder()
                .append("(")
                .append(parent.getId())
                .append(", ")
                .append(position)
                .append(")");
        Log.d(TAG, ": onItemSelected - (ID, position) = " + log);

        switch (parent.getId()) {
            case R.id.rotor1_option:
                mRotor1.setTurnover(EnigmaUtils.ENIGMA_ROTOR_TURNOVERS[position]);
                mRotor1.setForwardMap(EnigmaUtils.ENIGMA_ROTORS_F[position]);
                mRotor1.setReverseMap(EnigmaUtils.ENIGMA_ROTORS_R[position]);
                mRotor1.setIdentifier(EnigmaUtils.ENIGMA_ROTOR_OPTIONS[position]);
                break;
            case R.id.rotor2_option:
                mRotor2.setTurnover(EnigmaUtils.ENIGMA_ROTOR_TURNOVERS[position]);
                mRotor2.setForwardMap(EnigmaUtils.ENIGMA_ROTORS_F[position]);
                mRotor2.setReverseMap(EnigmaUtils.ENIGMA_ROTORS_R[position]);
                mRotor2.setIdentifier(EnigmaUtils.ENIGMA_ROTOR_OPTIONS[position]);
                break;
            case R.id.rotor3_option:
                mRotor3.setTurnover(EnigmaUtils.ENIGMA_ROTOR_TURNOVERS[position]);
                mRotor3.setForwardMap(EnigmaUtils.ENIGMA_ROTORS_F[position]);
                mRotor3.setReverseMap(EnigmaUtils.ENIGMA_ROTORS_R[position]);
                mRotor3.setIdentifier(EnigmaUtils.ENIGMA_ROTOR_OPTIONS[position]);
                break;
            case R.id.ring1_option:
                mRotor1.setRing(position);
                break;
            case R.id.ring2_option:
                mRotor2.setRing(position);
                break;
            case R.id.ring3_option:
                mRotor3.setRing(position);
                break;
        }
    }

    /**
     * Callback for rotor and ring selectors.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, ": onNothingSelected");
        // Do nothing
    }

    /**
     * Callback for when plugs are clicked in plugboard.
     * @param v
     */
    @Override
    public void onClick(View v) {
        StringBuilder log = new StringBuilder()
                .append("(")
                .append(v.getId())
                .append(")");
        Log.d(TAG, ": onClick - (view) = " + log);

        mSounds.playSound(EnigmaUtils.ENIGMA_SOUND_PLUG);
        Button curPlug = (Button)v;
        int plugLetter = letterToNumber(curPlug.getText().charAt(0));
        int lastPlugLetter = (mLastPlug != null) ?
                letterToNumber(mLastPlug.getText().charAt(0)) : -1;

        // If the button has a pair -> set default button for pair
        if (mPlugboard.hasPair(plugLetter)) {
            Button clickedPlugPair = mPlugArray[mPlugboard.getPair(plugLetter)];
            curPlug.setBackgroundResource(R.drawable.enigma_settings_plugboard_default);
            curPlug.setTextColor(Color.parseColor("#E0E0E0"));
            clickedPlugPair.setBackgroundResource(R.drawable.enigma_settings_plugboard_default);
            clickedPlugPair.setTextColor(Color.parseColor("#E0E0E0"));
            mPlugboard.removePair(plugLetter);

        }
        // Else if same button is clicked again, default color
        else if (plugLetter == lastPlugLetter && mFlag) {
            curPlug.setBackgroundResource(R.drawable.enigma_settings_plugboard_default);
            curPlug.setTextColor(Color.parseColor("#E0E0E0"));
            mFlag = false;
        }
        // Else if button is second pair -> color second button
        else if (mFlag) {
            int color = mPlugboard.addPair(plugLetter, lastPlugLetter);
            curPlug.setBackgroundResource(color);
            curPlug.setTextColor(Color.WHITE);
            mFlag = false;
        }
        // Else there is no pair, and this is first button hasn't been clicked yet
        else {
            curPlug.setBackgroundResource(mPlugboard.getTopColor());
            curPlug.setTextColor(Color.WHITE);
            mFlag = true;

        }
        mLastPlug = curPlug;
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
     * Setup views for Settings
     * @param view
     */
    private void setupViews(View view) {
        mReflectorRadiogroup = view.findViewById(R.id.reflector_option);
        mRotorSpinner1 = view.findViewById(R.id.rotor1_option);
        mRotorSpinner2 = view.findViewById(R.id.rotor2_option);
        mRotorSpinner3 = view.findViewById(R.id.rotor3_option);
        mRingSpinner1 = view.findViewById(R.id.ring1_option);
        mRingSpinner2 = view.findViewById(R.id.ring2_option);
        mRingSpinner3 = view.findViewById(R.id.ring3_option);

        ArrayAdapter<String> rotorAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, EnigmaUtils.ENIGMA_ROTOR_OPTIONS);
        Spinner[] slots = {mRotorSpinner1, mRotorSpinner2, mRotorSpinner3};
        for (Spinner spinner: slots) spinner.setAdapter(rotorAdapter);

        ArrayAdapter<String> ringAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, EnigmaUtils.ENIGMA_ROTOR_LETTERS);
        Spinner[] rings = {mRingSpinner1, mRingSpinner2, mRingSpinner3};
        for (Spinner ring: rings) ring.setAdapter(ringAdapter);

        Button mA = view.findViewById(R.id.lamp_a);
        Button mB = view.findViewById(R.id.lamp_b);
        Button mC = view.findViewById(R.id.lamp_c);
        Button mD = view.findViewById(R.id.lamp_d);
        Button mE = view.findViewById(R.id.lamp_e);
        Button mF = view.findViewById(R.id.lamp_f);
        Button mG = view.findViewById(R.id.lamp_g);
        Button mH = view.findViewById(R.id.lamp_h);
        Button mI = view.findViewById(R.id.lamp_i);
        Button mJ = view.findViewById(R.id.lamp_j);
        Button mK = view.findViewById(R.id.lamp_k);
        Button mL = view.findViewById(R.id.lamp_l);
        Button mN = view.findViewById(R.id.lamp_n);
        Button mM = view.findViewById(R.id.lamp_m);
        Button mO = view.findViewById(R.id.lamp_o);
        Button mP = view.findViewById(R.id.lamp_p);
        Button mQ = view.findViewById(R.id.lamp_q);
        Button mR = view.findViewById(R.id.lamp_r);
        Button mS = view.findViewById(R.id.lamp_s);
        Button mT = view.findViewById(R.id.lamp_t);
        Button mU = view.findViewById(R.id.lamp_u);
        Button mV = view.findViewById(R.id.lamp_v);
        Button mW = view.findViewById(R.id.lamp_w);
        Button mX = view.findViewById(R.id.lamp_x);
        Button mY = view.findViewById(R.id.lamp_y);
        Button mZ = view.findViewById(R.id.lamp_z);
        mPlugArray = new Button[]{mA, mB, mC, mD, mE, mF, mG, mH, mI, mJ, mK, mL, mM, mN, mO, mP, mQ, mR, mS, mT, mU, mV, mW, mX, mY, mZ};

    }

    /**
     * Setup initial values for settings.
     */
    private void setupInitialValues() {
        switch (mRotor1.getTurnover()) {
            case EnigmaUtils.ENIGMA_ROTOR_1_TURNOVER:
                mRotorSpinner1.setSelection(0);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_2_TURNOVER:
                mRotorSpinner1.setSelection(1);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_3_TURNOVER:
                mRotorSpinner1.setSelection(2);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_4_TURNOVER:
                mRotorSpinner1.setSelection(3);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_5_TURNOVER:
                mRotorSpinner1.setSelection(4);
                break;
        }
        switch (mRotor2.getTurnover()) {
            case EnigmaUtils.ENIGMA_ROTOR_1_TURNOVER:
                mRotorSpinner2.setSelection(0);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_2_TURNOVER:
                mRotorSpinner2.setSelection(1);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_3_TURNOVER:
                mRotorSpinner2.setSelection(2);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_4_TURNOVER:
                mRotorSpinner2.setSelection(3);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_5_TURNOVER:
                mRotorSpinner2.setSelection(4);
                break;
        }
        switch (mRotor3.getTurnover()) {
            case EnigmaUtils.ENIGMA_ROTOR_1_TURNOVER:
                mRotorSpinner3.setSelection(0);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_2_TURNOVER:
                mRotorSpinner3.setSelection(1);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_3_TURNOVER:
                mRotorSpinner3.setSelection(2);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_4_TURNOVER:
                mRotorSpinner3.setSelection(3);
                break;
            case EnigmaUtils.ENIGMA_ROTOR_5_TURNOVER:
                mRotorSpinner3.setSelection(4);
                break;
        }

        mRingSpinner1.setSelection(mRotor1.getRing());
        mRingSpinner2.setSelection(mRotor2.getRing());
        mRingSpinner3.setSelection(mRotor3.getRing());

        if (Arrays.equals(mReflector.getReflectorMap(), EnigmaUtils.ENIGMA_REFLECTOR_A)) {
            mReflectorRadiogroup.check(R.id.reflector_ukwa);
        }
        else if (Arrays.equals(mReflector.getReflectorMap(), EnigmaUtils.ENIGMA_REFLECTOR_B)) {
            mReflectorRadiogroup.check(R.id.reflector_ukwb);
        }
        else {
            mReflectorRadiogroup.check(R.id.reflector_ukwc);
        }

        // Set values of the plugboard
        Map<Integer, Integer> colorMap = mPlugboard.getColorMap();
        for (Map.Entry<Integer, Integer> entry : colorMap.entrySet()) {
            mPlugArray[entry.getKey()].setBackgroundResource(entry.getValue());
            mPlugArray[entry.getKey()].setTextColor(Color.WHITE);
        }
    }

    /**
     * Stores the initial settings. This is to compare if the settings are changed after.
     */
    private void storeInitialValues() {
        mInitialValues = new Bundle();
        mInitialValues.putString(EnigmaUtils.ENIGMA_KEY_SLOT1, mRotor1.getIdentifier());
        mInitialValues.putString(EnigmaUtils.ENIGMA_KEY_SLOT2, mRotor2.getIdentifier());
        mInitialValues.putString(EnigmaUtils.ENIGMA_KEY_SLOT3, mRotor3.getIdentifier());
        mInitialValues.putInt(EnigmaUtils.ENIGMA_KEY_RING1, mRotor1.getRing());
        mInitialValues.putInt(EnigmaUtils.ENIGMA_KEY_RING2, mRotor2.getRing());
        mInitialValues.putInt(EnigmaUtils.ENIGMA_KEY_RING3, mRotor3.getRing());
        mInitialValues.putString(EnigmaUtils.ENIGMA_KEY_REFLECTOR, mReflector.getIdentifer());
        mInitialValues.putString(EnigmaUtils.ENIGMA_KEY_PLUGBOARD, mPlugboard.getColorMap().toString());
    }

    /**
     * Determines if user has changed any settings.
     * @return
     */
    private boolean settingsChanged() {
        boolean flag = false;

        // Check rotors
        if (!mRotor1.getIdentifier().equals(mInitialValues.getString(EnigmaUtils.ENIGMA_KEY_SLOT1))) {
            flag = true;
        }
        if (!mRotor2.getIdentifier().equals(mInitialValues.getString(EnigmaUtils.ENIGMA_KEY_SLOT2))) {
            flag = true;
        }
        if (!mRotor3.getIdentifier().equals(mInitialValues.getString(EnigmaUtils.ENIGMA_KEY_SLOT3))) {
            flag = true;
        }

        // Check rings
        if (mRotor1.getRing() != mInitialValues.getInt(EnigmaUtils.ENIGMA_KEY_RING1)) {
            flag = true;
        }
        if (mRotor2.getRing() != mInitialValues.getInt(EnigmaUtils.ENIGMA_KEY_RING2)) {
            flag = true;
        }
        if (mRotor3.getRing() != mInitialValues.getInt(EnigmaUtils.ENIGMA_KEY_RING3)) {
            flag = true;
        }

        // Check reflector
        if (!mReflector.getIdentifer().equals(mInitialValues.getString(EnigmaUtils.ENIGMA_KEY_REFLECTOR))) {
            flag = true;
        }

        // Check plugboard
        if (!mPlugboard.getColorMap().toString().equals(mInitialValues.getString(EnigmaUtils.ENIGMA_KEY_PLUGBOARD))) {
            flag = true;
        }

        return flag;
    }


}