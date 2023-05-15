package com.smitpatel.enigmamachine;

public class EnigmaUtils {

        // Shared Preferences
        public static final String ENIGMA_MUTE = "enigma_mute";

        // Rotors - Turnover Position
        public static final String[] ENIGMA_ROTOR_OPTIONS = {"I", "II", "III", "IV", "V"};
        public static final int ENIGMA_ROTOR_1_TURNOVER = 16;
        public static final int ENIGMA_ROTOR_2_TURNOVER = 4;
        public static final int ENIGMA_ROTOR_3_TURNOVER = 21;
        public static final int ENIGMA_ROTOR_4_TURNOVER = 9;
        public static final int ENIGMA_ROTOR_5_TURNOVER = 25;
        public static final int[] ENIGMA_ROTOR_TURNOVERS = {ENIGMA_ROTOR_1_TURNOVER, ENIGMA_ROTOR_2_TURNOVER, ENIGMA_ROTOR_3_TURNOVER, ENIGMA_ROTOR_4_TURNOVER, ENIGMA_ROTOR_5_TURNOVER};

        // Rotor Letters
        public static final String[] ENIGMA_ROTOR_LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        // Rotors - Input Direction
        public static final int[] ENIGMA_1_ROTOR_1F = {4, 10, 12, 5, 11, 6, 3, 16, 21, 25, 13, 19, 14, 22, 24, 7, 23, 20, 18, 15, 0, 8, 1, 17, 2, 9};
        public static final int[] ENIGMA_1_ROTOR_2F = {0, 9, 3, 10, 18, 8, 17, 20, 23, 1, 11, 7, 22, 19, 12, 2, 16, 6, 25, 13, 15, 24, 5, 21, 14, 4};
        public static final int[] ENIGMA_1_ROTOR_3F = {1, 3, 5, 7, 9, 11, 2, 15, 17, 19, 23, 21, 25, 13, 24, 4, 8, 22, 6, 0, 10, 12, 20, 18, 16, 14};
        public static final int[] ENIGMA_1_ROTOR_4F = {4, 18, 14, 21, 15, 25, 9, 0, 24, 16, 20, 8, 17, 7, 23, 11, 13, 5, 19, 6, 10, 3, 2, 12, 22, 1};
        public static final int[] ENIGMA_1_ROTOR_5F = {21, 25, 1, 17, 6, 8, 19, 24, 20, 15, 18, 3, 13, 7, 11, 23, 0, 22, 12, 9, 16, 14, 5, 4, 2, 10};
        public static final int[][] ENIGMA_ROTORS_F = {ENIGMA_1_ROTOR_1F, ENIGMA_1_ROTOR_2F, ENIGMA_1_ROTOR_3F, ENIGMA_1_ROTOR_4F, ENIGMA_1_ROTOR_5F};

        // Rotors - Output Direction
        public static final int[] ENIGMA_1_ROTOR_1R = {20, 22, 24, 6, 0, 3, 5, 15, 21, 25, 1, 4, 2, 10, 12, 19, 7, 23, 18, 11, 17, 8, 13, 16, 14, 9};
        public static final int[] ENIGMA_1_ROTOR_2R = {0, 9, 15, 2, 25, 22, 17, 11, 5, 1, 3, 10, 14, 19, 24, 20, 16, 6, 4, 13, 7, 23, 12, 8, 21, 18};
        public static final int[] ENIGMA_1_ROTOR_3R = {19, 0, 6, 1, 15, 2, 18, 3, 16, 4, 20, 5, 21, 13, 25, 7, 24, 8, 23, 9, 22, 11, 17, 10, 14, 12};
        public static final int[] ENIGMA_1_ROTOR_4R = {7, 25, 22, 21, 0, 17, 19, 13, 11, 6, 20, 15, 23, 16, 2, 4, 9, 12, 1, 18, 10, 3, 24, 14, 8, 5};
        public static final int[] ENIGMA_1_ROTOR_5R = {16, 2, 24, 11, 23, 22, 4, 13, 5, 19, 25, 14, 18, 12, 21, 9, 20, 3, 10, 6, 8, 0, 17, 15, 7, 1};
        public static final int[][] ENIGMA_ROTORS_R = {ENIGMA_1_ROTOR_1R, ENIGMA_1_ROTOR_2R, ENIGMA_1_ROTOR_3R, ENIGMA_1_ROTOR_4R, ENIGMA_1_ROTOR_5R};


        // Reflectors
        public static final String[] ENIGMA_REFLECTOR_OPTIONS = {"UKW-A", "UKW-B", "UKW-C"};
        public static final int[] ENIGMA_REFLECTOR_A = {4, 9, 12, 25, 0, 11, 24, 23, 21, 1, 22, 5, 2, 17, 16, 20, 14, 13, 19, 18, 15, 8, 10, 7, 6, 3};
        public static final int[] ENIGMA_REFLECTOR_B = {24, 17, 20, 7, 16, 18, 11, 3, 15, 23, 13, 6, 14, 10, 12, 8, 4, 1, 5, 25, 2, 22, 21, 9, 0, 19};
        public static final int[] ENIGMA_REFLECTOR_C = {5, 21, 15, 9, 8, 0, 14, 24, 4, 3, 17, 25, 23, 22, 6, 2, 19, 10, 20, 16, 18, 1, 13, 12, 7, 11};

        // Plugboard Default
        public static final int[] ENIGMA_PLUGBOARD_DEFAULT = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        public static final int ENIGMA_PLUGBOARD_COLOR1 = R.drawable.enigma_settings_plugboard_color1;
        public static final int ENIGMA_PLUGBOARD_COLOR2 = R.drawable.enigma_settings_plugboard_color2;
        public static final int ENIGMA_PLUGBOARD_COLOR3 = R.drawable.enigma_settings_plugboard_color3;
        public static final int ENIGMA_PLUGBOARD_COLOR4 = R.drawable.enigma_settings_plugboard_color4;
        public static final int ENIGMA_PLUGBOARD_COLOR5 = R.drawable.enigma_settings_plugboard_color5;
        public static final int ENIGMA_PLUGBOARD_COLOR6 = R.drawable.enigma_settings_plugboard_color6;
        public static final int ENIGMA_PLUGBOARD_COLOR7 = R.drawable.enigma_settings_plugboard_color7;
        public static final int ENIGMA_PLUGBOARD_COLOR8 = R.drawable.enigma_settings_plugboard_color8;
        public static final int ENIGMA_PLUGBOARD_COLOR9 = R.drawable.enigma_settings_plugboard_color9;
        public static final int ENIGMA_PLUGBOARD_COLOR10 = R.drawable.enigma_settings_plugboard_color10;
        public static final int ENIGMA_PLUGBOARD_COLOR11 = R.drawable.enigma_settings_plugboard_color11;
        public static final int ENIGMA_PLUGBOARD_COLOR12 = R.drawable.enigma_settings_plugboard_color12;
        public static final int ENIGMA_PLUGBOARD_COLOR13 = R.drawable.enigma_settings_plugboard_color13;

        // Bundle Keys
        public static final String ENIGMA_KEY_REFLECTOR = "enigma_reflector";
        public static final String ENIGMA_KEY_SLOT1 = "enigma_slot1";
        public static final String ENIGMA_KEY_SLOT2 = "enigma_slot2";
        public static final String ENIGMA_KEY_SLOT3 = "enigma_slot3";
        public static final String ENIGMA_KEY_RING1 = "enigma_ring1";
        public static final String ENIGMA_KEY_RING2 = "enigma_ring2";
        public static final String ENIGMA_KEY_RING3 = "enigma_ring3";
        public static final String ENIGMA_KEY_PLUGBOARD = "enigma_plugboard";

        // Sound Effect Constants
        public static final int ENIGMA_SOUND_DEFAULT = 0;
        public static final int ENIGMA_SOUND_KEY = 1;
        public static final int ENIGMA_SOUND_SPACE = 2;
        public static final int ENIGMA_SOUND_DELETE = 3;
        public static final int ENIGMA_SOUND_ROTOR = 4;
        public static final int ENIGMA_SOUND_PLUG = 5;
        public static final int ENIGMA_SOUND_CHANGES = 6;




}
