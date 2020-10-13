package com.smitpatel.enigmamachine;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundEffects {

    private static SoundEffects mSingleton = null;
    private SoundPool mSoundPool;
    private int mDefaultSound, mKeySound, mSpaceSound, mDeleteSound, mRotorSound, mPlugSound, mChangesSound;
    private boolean mDefaultFlag, mKeyFlag, mSpaceFlag, mDeleteFlag, mRotorFlag, mPlugFlag, mChangesFlag;

    /**
     * Constructor
     * @param context
     */
    private SoundEffects(Context context) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(7)
                .build();

        // Load sounds
        mKeySound = mSoundPool.load(context, R.raw.key_press,1);
        mSpaceSound = mSoundPool.load(context, R.raw.spacebar_press,1);
        mDeleteSound = mSoundPool.load(context, R.raw.delete_sound,1);
        mRotorSound = mSoundPool.load(context, R.raw.rotor_shift,1);
        mDefaultSound = mSoundPool.load(context,R.raw.default_press,1);
        mChangesSound = mSoundPool.load(context, R.raw.changes_sound,1);
        mPlugSound = mSoundPool.load(context, R.raw.plug_press,1);

        // Ensure they are loaded by SoundPool class
        mKeyFlag = false;
        mSpaceFlag = false;
        mDeleteFlag = false;
        mRotorFlag = false;
        mDefaultFlag = false;
        mChangesFlag = false;
        mPlugFlag = false;

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if ((sampleId == mKeySound) && (status == 0)) mKeyFlag = true;
                else if ((sampleId == mSpaceSound) && (status == 0)) mSpaceFlag = true;
                else if ((sampleId == mDeleteSound) && (status == 0)) mDeleteFlag = true;
                else if ((sampleId == mRotorSound) && (status == 0)) mRotorFlag = true;
                else if ((sampleId == mDefaultSound) && (status == 0)) mDefaultFlag = true;
                else if ((sampleId == mPlugSound) && (status == 0)) mPlugFlag = true;
                else if ((sampleId == mChangesSound) && (status == 0)) mChangesFlag = true;
            }
        });

    }

    /**
     * Get singleton for Sound Effects class.
     * @param context
     * @return
     */
    public static SoundEffects getInstance(Context context) {
        if (mSingleton == null) {
            mSingleton = new SoundEffects(context);
        }
        return mSingleton;
    }

    /**
     * This method should be used after context using SoundEffects class is going away.
     */
    public void shutDown() {
        mSoundPool.release();
        mSoundPool = null;
    }

    /**
     * Plays sound for given soundID. SoundIDs must be from EnigmaUtils class
     * @param soundID
     */
    public void playSound(int soundID) {
        int soundLoad = -1;
        boolean playFlag = false;

        switch (soundID) {
            case EnigmaUtils.ENIGMA_SOUND_KEY:
                soundLoad = mKeySound;
                playFlag = mKeyFlag;
                break;
            case EnigmaUtils.ENIGMA_SOUND_SPACE:
                soundLoad = mSpaceSound;
                playFlag = mSpaceFlag;
                break;
            case EnigmaUtils.ENIGMA_SOUND_DELETE:
                soundLoad = mDeleteSound;
                playFlag = mDeleteFlag;
                break;
            case EnigmaUtils.ENIGMA_SOUND_ROTOR:
                soundLoad = mRotorSound;
                playFlag = mRotorFlag;
                break;
            case EnigmaUtils.ENIGMA_SOUND_DEFAULT:
                soundLoad = mDefaultSound;
                playFlag = mDefaultFlag;
                break;
            case EnigmaUtils.ENIGMA_SOUND_CHANGES:
                soundLoad = mChangesSound;
                playFlag = mChangesFlag;
                break;
            case EnigmaUtils.ENIGMA_SOUND_PLUG:
                soundLoad = mPlugSound;
                playFlag = mPlugFlag;
                break;
        }

        // only play the sound if the sound has been loaded by SoundPool.
        if (playFlag) {
            mSoundPool.play(soundLoad, 1,1,0,0, 1);
        }
    }

}
