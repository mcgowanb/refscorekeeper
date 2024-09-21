package com.mcgowanb.projects.refereescorekeeper.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.wear.compose.material.*
import com.mcgowanb.projects.refereescorekeeper.model.GameTimeViewModel
import com.mcgowanb.projects.refereescorekeeper.model.GameViewModel
import com.mcgowanb.projects.refereescorekeeper.ui.GameActionOverlay

@Composable
fun ConfirmationDialog(
    confirmationQuestion: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
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
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                    shape = CircleShape,
                    modifier = Modifier.size(60.dp)
                ) {
                    Text("No", color = Color.White)
                }
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3D84FF)),
                    shape = CircleShape,
                    modifier = Modifier.size(60.dp)
                ) {
                    Text("Yes", color = Color.White)
                }
            }
        }
    }
}

@Preview(device = "id:wearos_small_round")
@Composable
private fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        "Reset Game?",
        {},
        {}
    )
}
