package com.smitpatel.enigmamachine.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.smitpatel.enigmamachine.R
import com.smitpatel.enigmamachine.databinding.ActivityEnigmaMainBinding
import com.smitpatel.enigmamachine.ui.EnigmaSounds
import com.smitpatel.enigmamachine.ui.SoundEffects
import com.smitpatel.enigmamachine.ui.setting.SettingsFragment
import com.smitpatel.enigmamachine.events.EnigmaEvent
import com.smitpatel.enigmamachine.getRotorLabelText
import com.smitpatel.enigmamachine.letterToNumber
import com.smitpatel.enigmamachine.models.EnigmaHistoryItem
import com.smitpatel.enigmamachine.models.Rotor
import com.smitpatel.enigmamachine.toNumber
import com.smitpatel.enigmamachine.toReflector
import com.smitpatel.enigmamachine.toRotorOption
import com.smitpatel.enigmamachine.toSettingsJson
import com.smitpatel.enigmamachine.toSettingsString
import com.smitpatel.enigmamachine.ui.RotorPosition
import com.smitpatel.enigmamachine.ui.paste_error.PasteErrorFragment
import com.smitpatel.enigmamachine.viewmodels.EnigmaViewModel
import org.json.JSONObject
import java.lang.Exception
import java.util.Stack

class EnigmaMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnigmaMainBinding

    private val viewModel : EnigmaViewModel by viewModels()

    private val sounds : SoundEffects = SoundEffects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnigmaMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        setupRenderer()
        lifecycle.addObserver(sounds)
        SoundEffects.initialize(context = applicationContext)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.handleEvent(EnigmaEvent.RestoreState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.handleEvent(EnigmaEvent.SaveState)
        super.onSaveInstanceState(outState)
    }

    private fun setupRenderer() {

        fun showToast(text: String) {
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            viewModel.handleEvent(EnigmaEvent.ToastMessageDisplayed)
        }

        viewModel.uiState.observe(this) {
            binding.rotors.rotor1.value = it.rotorOnePosition + 1
            binding.rotors.rotor2.value = it.rotorTwoPosition + 1
            binding.rotors.rotor3.value = it.rotorThreePosition + 1

            binding.rotors.rotor1Label.text = it.rotorOneLabel.getRotorLabelText(applicationContext)
            binding.rotors.rotor2Label.text = it.rotorTwoLabel.getRotorLabelText(applicationContext)
            binding.rotors.rotor3Label.text = it.rotorThreeLabel.getRotorLabelText(applicationContext)

            binding.textboxes.textRaw.setText(it.rawMessage)
            binding.textboxes.textCode.setText(it.encodedMessage)

            binding.textboxes.textRaw.setSelection(it.rawMessage.length)
            binding.textboxes.textCode.setSelection(it.encodedMessage.length)

            val lamps = arrayOf(
                binding.lampboard.lampA, binding.lampboard.lampB,
                binding.lampboard.lampC, binding.lampboard.lampD, binding.lampboard.lampE,
                binding.lampboard.lampF, binding.lampboard.lampG, binding.lampboard.lampH,
                binding.lampboard.lampI, binding.lampboard.lampJ, binding.lampboard.lampK,
                binding.lampboard.lampL, binding.lampboard.lampM, binding.lampboard.lampN,
                binding.lampboard.lampO, binding.lampboard.lampP, binding.lampboard.lampQ,
                binding.lampboard.lampR, binding.lampboard.lampS, binding.lampboard.lampT,
                binding.lampboard.lampU, binding.lampboard.lampV, binding.lampboard.lampW,
                binding.lampboard.lampX, binding.lampboard.lampY, binding.lampboard.lampZ,
            )

            lamps.forEach { lamp ->
                lamp.isPressed = false
            }

            if (it.activeLampboard != -1) {
                lamps[it.activeLampboard].isPressed = true
            }

            if (it.showSettingsChangedToast) {
                showToast(text = resources.getString(R.string.settings_changed_toast_message))
                sounds.playSound(sound = EnigmaSounds.CHANGES)
                viewModel.handleEvent(
                    event = EnigmaEvent.ToastMessageDisplayed
                )
            }

            if (it.showSettingsErrorToast) {
                showToast(text = resources.getString(R.string.settings_not_changed_toast_message))
                viewModel.handleEvent(
                    event = EnigmaEvent.ToastMessageDisplayed
                )
            }

            if (it.clipboardCopyState != null) {
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val settingsState = it.clipboardCopyState.settingsState
                val json = it.clipboardCopyState.json
                when {
                    settingsState == null -> clipboardManager.setPrimaryClip(ClipData.newPlainText("", it.clipboardCopyState.text))
                    json -> clipboardManager.setPrimaryClip(ClipData.newPlainText("", settingsState.toSettingsJson(applicationContext)))
                    else -> clipboardManager.setPrimaryClip(ClipData.newPlainText("", settingsState.toSettingsString(applicationContext)))
                }

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    showToast(getString(R.string.text_copied_toast_message))
                } else {
                    viewModel.handleEvent(EnigmaEvent.ToastMessageDisplayed)
                }
            }

            if (it.pasteError != null) {
                PasteErrorFragment(
                    pasteString = it.pasteError,
                    onCloseClick = {
                        viewModel.handleEvent(EnigmaEvent.ClosePasteError)
                    },
                    onSubmitClick = { newPasteText ->
                        viewModel.handleEvent(EnigmaEvent.ClosePasteError)
                        viewModel.handleEvent(EnigmaEvent.PasteRawText(rawText = newPasteText))
                    }
                ).show(supportFragmentManager, "")
            }
        }
    }

    private fun setupViews() {

        binding.rotors.rotor1.displayedValues = resources.getStringArray(R.array.rotor_values)
        binding.rotors.rotor2.displayedValues = resources.getStringArray(R.array.rotor_values)
        binding.rotors.rotor3.displayedValues = resources.getStringArray(R.array.rotor_values)

        binding.rotors.rotor1.setOnValueChangedListener { _, _, newValue ->
            SoundEffects.playSound(sound = EnigmaSounds.ROTOR)
            viewModel.handleEvent(
                event = EnigmaEvent.RotorStartPositionChanged(
                    start = newValue - 1,
                    rotorPosition = RotorPosition.ONE
                )
            )
        }

        binding.rotors.rotor2.setOnValueChangedListener { _, _, newValue ->
            SoundEffects.playSound(sound = EnigmaSounds.ROTOR)
            viewModel.handleEvent(
                event = EnigmaEvent.RotorStartPositionChanged(
                    start = newValue - 1,
                    rotorPosition = RotorPosition.TWO
                )
            )
        }

        binding.rotors.rotor3.setOnValueChangedListener { _, _, newValue ->
            SoundEffects.playSound(sound = EnigmaSounds.ROTOR)
            viewModel.handleEvent(
                event = EnigmaEvent.RotorStartPositionChanged(
                    start = newValue - 1,
                    rotorPosition = RotorPosition.THREE
                )
            )
        }

        binding.rotors.powerSwitch.setOnClickListener {
            SoundEffects.playSound(sound = EnigmaSounds.DEFAULT)
            SettingsFragment().show(supportFragmentManager, "")
        }

        registerForContextMenu(binding.textboxes.textfields)

        val buttons = arrayOf(
            binding.keyboard.buttonA, binding.keyboard.buttonB, binding.keyboard.buttonC,
            binding.keyboard.buttonD, binding.keyboard.buttonE, binding.keyboard.buttonF,
            binding.keyboard.buttonG, binding.keyboard.buttonH, binding.keyboard.buttonI,
            binding.keyboard.buttonJ, binding.keyboard.buttonK, binding.keyboard.buttonL,
            binding.keyboard.buttonM, binding.keyboard.buttonN, binding.keyboard.buttonO,
            binding.keyboard.buttonP, binding.keyboard.buttonQ, binding.keyboard.buttonR,
            binding.keyboard.buttonS, binding.keyboard.buttonT, binding.keyboard.buttonU,
            binding.keyboard.buttonV, binding.keyboard.buttonW, binding.keyboard.buttonX,
            binding.keyboard.buttonY, binding.keyboard.buttonZ, binding.keyboard.buttonDelete,
            binding.keyboard.buttonSpacebar
        )

        buttons.forEach { button ->
            button.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        button.isPressed = true
                        button.performClick()
                        when (button.id) {
                            R.id.button_delete -> {
                                SoundEffects.playSound(sound = EnigmaSounds.DELETE)
                                viewModel.handleEvent(
                                    event = EnigmaEvent.InputDeletePressed
                                )
                                return@setOnTouchListener false
                            }
                            R.id.button_spacebar -> {
                                SoundEffects.playSound(sound = EnigmaSounds.SPACE)
                                viewModel.handleEvent(
                                    event = EnigmaEvent.InputSpacePressed
                                )
                            }
                            else -> {
                                SoundEffects.playSound(sound = EnigmaSounds.KEY)
                                viewModel.handleEvent(
                                    event = EnigmaEvent.InputKeyPressed(
                                        input = button.text[0].letterToNumber()
                                    )
                                )
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        button.isPressed = false
                        if ((button.id != R.id.button_delete) && (button.id !=R.id.button_spacebar)) {
                            viewModel.handleEvent(
                                EnigmaEvent.InputKeyLifted(
                                    input = button.text[0].letterToNumber()
                                )
                            )
                        }
                    }
                }
                true
            }
        }

        binding.keyboard.buttonDelete.setOnLongClickListener {
            viewModel.handleEvent(EnigmaEvent.InputLongDeletePressed)
            true
        }

        binding.textboxes.textRaw.showSoftInputOnFocus = false
        binding.textboxes.textCode.showSoftInputOnFocus = false

        binding.textboxes.textRaw.requestFocus()
        binding.textboxes.textRaw.requestFocus()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.enigma_options_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.copy_raw_text -> {
                viewModel.handleEvent(EnigmaEvent.CopyRawText)
                true
            }
            R.id.copy_encoded_text -> {
                viewModel.handleEvent(EnigmaEvent.CopyEncodedText)
                true
            }
            R.id.copy_enigma_settings_text -> {
                viewModel.handleEvent(EnigmaEvent.CopySettingsText)
                true
            }
            R.id.copy_enigma_settings_json -> {
                viewModel.handleEvent(EnigmaEvent.CopySettingsJson)
                true
            }
            R.id.paste_raw_text -> {
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipboardText = clipboardManager.primaryClip?.getItemAt(0)?.text
                if (!clipboardText.isNullOrEmpty()) {
                    viewModel.handleEvent(EnigmaEvent.PasteRawText(rawText = clipboardText.toString()))
                }
                true
            }
            R.id.paste_enigma_settings -> {
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipboardText = clipboardManager.primaryClip?.getItemAt(0)?.text

                if (!clipboardText.isNullOrEmpty()) {

                    try {
                        val json = JSONObject(clipboardText.toString())

                        val jsonRotorOptions = json.getJSONArray("rotorOptions")
                        val rotorOptions = (0 until jsonRotorOptions.length()).map {
                            jsonRotorOptions.getString(it).toRotorOption(applicationContext)
                        }

                        // Need to ensure these are within the
                        val jsonRotorPositions = json.getJSONArray("rotorPositions")
                        val rotorPositions = (0 until jsonRotorPositions.length()).map {
                            jsonRotorPositions.getString(it).toNumber()
                        }

                        val jsonRingPositions = json.getJSONArray("ringPositions")
                        val ringPositions = (0 until jsonRingPositions.length()).map {
                            jsonRingPositions.getString(it).toNumber()
                        }

                        val reflector = json.getString("reflector").toReflector(applicationContext)

                        val jsonPlugboardPairs = json.getJSONArray("plugboardPairs")
                        val plugboardPairs = mutableSetOf<Pair<Int, Int>>()
                        for (i in 0 until jsonPlugboardPairs.length()) {
                            val jsonPair = jsonPlugboardPairs.getJSONArray(i)
                            plugboardPairs.add(
                                Pair(
                                    first = jsonPair.getInt(0),
                                    second = jsonPair.getInt(1)
                                )
                            )
                        }

                        viewModel.handleEvent(EnigmaEvent.PasteEnigmaSettings(
                            settings = EnigmaHistoryItem(
                                rotorOneOption = rotorOptions[0],
                                rotorTwoOption = rotorOptions[1],
                                rotorThreeOption = rotorOptions[2],
                                rotorOnePosition = rotorPositions[0],
                                rotorTwoPosition = rotorPositions[1],
                                rotorThreePosition = rotorPositions[2],
                                ringOneOption = ringPositions[0],
                                ringTwoOption = ringPositions[1],
                                ringThreeOption = ringPositions[2],
                                reflectorOption = reflector,
                                plugboardPairs = plugboardPairs,
                            )
                        ))
                    } catch (e : Exception) {
                    }
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
}