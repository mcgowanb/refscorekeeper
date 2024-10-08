package com.mcgowanb.projects.refereescorekeeper.ui.button

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text


@Composable
fun MenuItem(
    label: String,
    value: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Chip(
        modifier = modifier,
        onClick = onClick,
        colors = ChipDefaults.secondaryChipColors(),
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(label)
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(value)
                }
            }
        },
        icon = { icon() }
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(device = "id:wearos_small_round", showSystemUi = true)
private fun MenuItemPreview() {
    MenuItem(
        label = "Label",
        value = "value",
        onClick = {},
        icon = {
            Icon(
                Icons.Rounded.Timer,
                contentDescription = "Set period time"
            )
        },
        modifier =  Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 2.dp)
    )
}