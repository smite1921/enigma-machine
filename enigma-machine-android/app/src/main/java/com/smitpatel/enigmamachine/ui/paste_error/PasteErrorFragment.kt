package com.smitpatel.enigmamachine.ui.paste_error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.smitpatel.enigmamachine.databinding.FragmentPasteErrorBinding

class PasteErrorFragment(
    private val pasteString: String,
    private val onCloseClick: () -> Unit,
    private val onSubmitClick: (String) -> Unit,
) : DialogFragment() {

    private var _binding: FragmentPasteErrorBinding? = null

    // Note: This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasteErrorBinding.inflate(inflater, container, false)
        binding.resolvePasteText.setText(pasteString)
        binding.closePaste.setOnClickListener {
            onCloseClick()
            dialog?.dismiss()
        }
        binding.submitPaste.setOnClickListener {
            onSubmitClick(binding.resolvePasteText.text.toString())
            dialog?.dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}