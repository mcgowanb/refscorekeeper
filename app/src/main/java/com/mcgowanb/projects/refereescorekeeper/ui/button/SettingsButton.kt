package com.mcgowanb.projects.refereescorekeeper.ui.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.mcgowanb.projects.refereescorekeeper.model.Setting

@Composable
fun SettingsButton(
    setting: Setting,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(5.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f),
                imageVector = setting.icon,
                contentDescription = setting.description,
                tint = Color.White
            )
            Text(
                modifier = Modifier
                    .weight(4f)
                    .padding(start = 10.dp),
                text = setting.description,
                color = Color.White
            )
            if (setting.value >= 0) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = setting.value.toString(),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
@Preview(device = "id:wearos_small_round")
fun SettingsPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        SettingsButton(
            Setting("periods", "Periods", Icons.Rounded.Functions, 2),
            {}
        )
    }
}