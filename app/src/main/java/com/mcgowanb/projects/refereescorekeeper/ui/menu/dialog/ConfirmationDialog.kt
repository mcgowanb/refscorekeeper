package com.mcgowanb.projects.refereescorekeeper.ui.menu.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.ui.animation.SlideLeftToRight

@Composable
fun ConfirmationDialog(
    confirmationQuestion: String,
    subText: String?,
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    SlideLeftToRight(visible = visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    confirmationQuestion,
                    style = MaterialTheme.typography.title2,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                if (!subText.isNullOrEmpty()) {
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.caption2,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = WearColors.DismissRed,
                            contentColor = WearColors.White
                        ),
                        modifier = Modifier
                            .size(ButtonDefaults.DefaultButtonSize)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = WearColors.ConfirmGreen,
                            contentColor = WearColors.White
                        ),
                        modifier = Modifier
                            .size(ButtonDefaults.DefaultButtonSize)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Done,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
private fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        confirmationQuestion = "Defaults?",
        subText = "",
        visible = true,
        {},
        {}
    )
}
//reset to defaults does not reset the number of periods to 2, it stays at whatever it was
