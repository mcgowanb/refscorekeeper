package com.mcgowanb.projects.refereescorekeeper.ui.menu.button

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.SwitchDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.ui.animation.MenuItemAnimation

@Composable
fun ToggleButton(
    title: String,
    secondaryText: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    visible: Boolean
) {
    MenuItemAnimation(
        visible = visible
    ) {
        ToggleChip(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 2.dp),
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            label = {
                Text(
                    title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            },
            secondaryLabel = {
                Text(
                    secondaryText,
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
            },
            colors = ToggleChipDefaults.toggleChipColors(
                checkedStartBackgroundColor = WearColors.Purple.copy(alpha = 0.5f),
                checkedEndBackgroundColor = WearColors.Purple,
                uncheckedStartBackgroundColor = WearColors.Purple.copy(alpha = 0.5f),
                uncheckedEndBackgroundColor = WearColors.Purple.copy(alpha = 0.5f)
            ),
            toggleControl = {
                Switch(
                    checked = isChecked,
                    onCheckedChange = null,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = WearColors.Pink,
                        checkedTrackColor = WearColors.Pink.copy(alpha = 0.5f),
                        uncheckedThumbColor = WearColors.Pink,
                        uncheckedTrackColor = WearColors.Pink.copy(alpha = 0.5f)
                    )
                )
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(device = "id:wearos_small_round", showSystemUi = true)
private fun ToggleButtonPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        ToggleButton(
            title = "Show Clock",
            secondaryText = "main clock",
            isChecked = true,
            onCheckedChange = {},
            visible = true
        )
    }
}
