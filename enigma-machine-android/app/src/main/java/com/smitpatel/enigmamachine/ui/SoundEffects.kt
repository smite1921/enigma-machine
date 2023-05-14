package com.smitpatel.enigmamachine.ui

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.smitpatel.enigmamachine.R

// TODO: Make Sound Effects independent of the phone mode i.e (vibrate, silent, ring).
object SoundEffects : DefaultLifecycleObserver {

    private const val ENIGMA_SOUND_PREFERENCES = "enigma_sound_preferences"
    private const val ENIGMA_SOUND_MUTE = "enigma_sound_is_mute_on"

    private lateinit var sharedPreferences : SharedPreferences

    private var soundPool : SoundPool? = null
    var isMuteOn : Boolean = false

    private var defaultSoundId : Int = -1
    private var keySoundId : Int = -1
    private var spaceSoundId : Int = -1
    private var deleteSoundId : Int = -1
    private var rotorSoundId : Int = -1
    private var plugboardSoundId : Int = -1
    private var changesMadeSoundId : Int = -1

    private var defaultSoundFlag : Boolean = false
    private var keySoundFlag : Boolean = false
    private var spaceSoundFlag : Boolean = false
    private var deleteSoundFlag : Boolean = false
    private var rotorSoundFlag : Boolean = false
    private var plugboardSoundFlag : Boolean = false
    private var changesMadeSoundFlag : Boolean = false

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(ENIGMA_SOUND_PREFERENCES, Context.MODE_PRIVATE)

        isMuteOn = sharedPreferences.getBoolean(ENIGMA_SOUND_MUTE,false)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(attributes)
            .setMaxStreams(7)
            .build()

        soundPool?.let {
            defaultSoundId = it.load(context, R.raw.default_press, 1)
            keySoundId = it.load(context, R.raw.key_press, 1)
            spaceSoundId = it.load(context, R.raw.spacebar_press, 1)
            deleteSoundId = it.load(context, R.raw.delete_sound, 1)
            rotorSoundId = it.load(context, R.raw.rotor_shift, 1)
            plugboardSoundId = it.load(context, R.raw.plug_press, 1)
            changesMadeSoundId = it.load(context, R.raw.changes_sound, 1)

            it.setOnLoadCompleteListener { _, sampleId, status ->
                when {
                    (sampleId == defaultSoundId) && (status == 0) -> defaultSoundFlag = true
                    (sampleId == keySoundId) && (status == 0) -> keySoundFlag = true
                    (sampleId == spaceSoundId) && (status == 0) -> spaceSoundFlag = true
                    (sampleId == deleteSoundId) && (status == 0) -> deleteSoundFlag = true
                    (sampleId == rotorSoundId) && (status == 0) -> rotorSoundFlag = true
                    (sampleId == plugboardSoundId) && (status == 0) -> plugboardSoundFlag = true
                    (sampleId == changesMadeSoundId) && (status == 0) -> changesMadeSoundFlag = true
                }
            }
        }

    }

    override fun onPause(owner: LifecycleOwner) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(ENIGMA_SOUND_MUTE, isMuteOn)
        editor.apply()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        soundPool?.release()
        soundPool = null
    }

    fun playSound(sound : EnigmaSounds) {
        if (!isMuteOn) {
            val playFlag: Boolean
            val soundId: Int
            when (sound) {
                EnigmaSounds.DEFAULT -> {
                    playFlag = defaultSoundFlag
                    soundId = defaultSoundId
                }
                EnigmaSounds.KEY -> {
                    playFlag = keySoundFlag
                    soundId = keySoundId
                }
                EnigmaSounds.SPACE -> {
                    playFlag = spaceSoundFlag
                    soundId = spaceSoundId
                }
                EnigmaSounds.DELETE -> {
                    playFlag = deleteSoundFlag
                    soundId = deleteSoundId
                }
                EnigmaSounds.ROTOR -> {
                    playFlag = rotorSoundFlag
                    soundId = rotorSoundId
                }
                EnigmaSounds.PLUGBOARD -> {
                    playFlag = plugboardSoundFlag
                    soundId = plugboardSoundId
                }
                EnigmaSounds.CHANGES -> {
                    playFlag = changesMadeSoundFlag
                    soundId = changesMadeSoundId
                }
            }

            if (playFlag) {
                soundPool?.play(soundId, 1f, 1f, 0, 0, 1f)
            }
        }
    }

}

enum class EnigmaSounds {
    DEFAULT,
    KEY,
    SPACE,
    DELETE,
    ROTOR,
    PLUGBOARD,
    CHANGES,
}