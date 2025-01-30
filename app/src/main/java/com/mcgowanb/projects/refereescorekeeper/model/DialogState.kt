package com.mcgowanb.projects.refereescorekeeper.model

sealed class DialogState {
    data object Hidden : DialogState()
    data class Confirmation(
        val title: String,
        val subText: String = "",
        val onConfirm: () -> Unit
    ) : DialogState()

    data class NumberPicker(
        val title: String,
        val initialValue: Int,
        val range: IntRange,
        val onConfirm: (Int) -> Unit
    ) : DialogState()

    data object Report : DialogState()
}