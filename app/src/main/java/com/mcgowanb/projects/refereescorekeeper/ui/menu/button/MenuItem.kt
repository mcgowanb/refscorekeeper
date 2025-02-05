package com.mcgowanb.projects.refereescorekeeper.ui.menu.button

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.ui.animation.MenuItemAnimation


@Composable
fun MenuItem(
    label: String,
    value: String? = null,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    visible: Boolean,
    backgroundColor: Color = WearColors.Purple.copy(alpha = 0.5f),
    iconTint: Color = WearColors.Pink
) {
    MenuItemAnimation(
        visible = visible
    ) {
        Chip(
            modifier = modifier,
            onClick = onClick,
            colors = ChipDefaults.chipColors(
                backgroundColor = backgroundColor,
            ),
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
                        if (value != null) {
                            Text(value)
                        }
                    }
                }
            },
            icon = {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = "",
                        tint = iconTint
                    )
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(device = "id:wearos_small_round", showSystemUi = true)
private fun MenuItemPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        MenuItem(
            label = "Label",
            value = "value",
            onClick = {},
            visible = true,
            icon = Icons.AutoMirrored.Rounded.ViewList,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 2.dp)
        )
    }
}