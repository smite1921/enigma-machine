package com.smitpatel.enigmamachine.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.smitpatel.enigmamachine.R

object SoundEffects : DefaultLifecycleObserver {

    lateinit var lifecycle: Lifecycle

    private var soundPool : SoundPool? = null

    private var defaultSoundId : Int = -1
    private var keySoundId : Int = -1
    private var spaceSoundId : Int = -1
    private var deleteSoundId : Int = -1
    private var rotorSoundId : Int = -1

    private var defaultSoundFlag : Boolean = false
    private var keySoundFlag : Boolean = false
    private var spaceSoundFlag : Boolean = false
    private var deleteSoundFlag : Boolean = false
    private var rotorSoundFlag : Boolean = false

    fun initialize(context: Context) {
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

            it.setOnLoadCompleteListener { _, sampleId, status ->
                when {
                    (sampleId == defaultSoundId) && (status == 0) -> defaultSoundFlag = true
                    (sampleId == keySoundId) && (status == 0) -> keySoundFlag = true
                    (sampleId == spaceSoundId) && (status == 0) -> spaceSoundFlag = true
                    (sampleId == deleteSoundId) && (status == 0) -> deleteSoundFlag = true
                    (sampleId == rotorSoundId) && (status == 0) -> rotorSoundFlag = true
                }
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        soundPool?.release()
        soundPool = null
    }

    fun playSound(sound : EnigmaSounds) {
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
        }

        if (playFlag) {
            soundPool?.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

}

enum class EnigmaSounds {
    DEFAULT,
    KEY,
    SPACE,
    DELETE,
    ROTOR,
}