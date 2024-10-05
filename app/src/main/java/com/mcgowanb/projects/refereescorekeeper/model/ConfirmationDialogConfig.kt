package com.mcgowanb.projects.refereescorekeeper.model

data class ConfirmationDialogConfig(
    val title: String,
    val action: () -> Unit
)
