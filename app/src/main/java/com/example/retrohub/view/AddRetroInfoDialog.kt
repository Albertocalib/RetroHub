package com.example.retrohub.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.retrohub.R
import com.example.retrohub.extensions.getString
import com.google.android.material.textfield.TextInputLayout

fun newInstance(
    action: AddRetroInfoDialog.Action,
    fieldsList: List<String> = emptyList(),
    index: Int = -1, text: String = "",
    selectedType: String = ""
) =
    AddRetroInfoDialog().apply {
        callback = action
        fields = fieldsList
        selectedItem = index to text
        selected = selectedType
    }

class AddRetroInfoDialog : DialogFragment() {

    interface Action {
        fun onSaveClick(text: String, field: String, index: Int)
    }

    lateinit var callback: Action
    lateinit var fields: List<String>
    lateinit var selectedItem: Pair<Int, String>
    lateinit var selected: String
    private lateinit var dialogView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val update = selectedItem.first > -1
            builder.setView(
                inflater.inflate(
                    R.layout.dialog_add_info,
                    null
                ).also { v -> dialogView = v })
                .also {
                    if (update) dialogView.findViewById<TextInputLayout>(R.id.description_input)
                        .editText?.setText(selectedItem.second)
                }
                .setPositiveButton(R.string.accept_button) { _, _ ->
                    callback.onSaveClick(
                        dialogView.findViewById<TextInputLayout>(R.id.description_input).getString(),
                        selected,
                        selectedItem.first
                    )
                }
                .setNegativeButton(R.string.decline_button) { _, _ ->
                    dialog?.cancel()
                }
            if (!update) {
                builder.setSingleChoiceItems(fields.toTypedArray(), -1) { _, i ->
                    selected = fields[i]
                }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}