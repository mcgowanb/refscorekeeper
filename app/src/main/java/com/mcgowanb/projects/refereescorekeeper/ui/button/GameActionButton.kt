package com.mcgowanb.projects.refereescorekeeper.ui.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.RotateLeft
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.RotateLeft
import androidx.compose.material.icons.rounded.SportsScore
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.model.GameAction
import com.mcgowanb.projects.refereescorekeeper.model.Setting

@Composable
fun GameActionButton(
    title: String,
    icon: ImageVector,
    action: () -> Unit,
    iconColor: Color,
    backgroundColor: Color
) {
    Button(
        onClick = action,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(5.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
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
                    .fillMaxSize()
                    .weight(2f)
                    ,
                imageVector = icon,
                contentDescription = "",
                tint = iconColor
            )
            Text(
                modifier = Modifier
                    .weight(6f)
                    .padding(start = 15.dp),
                text = title,
                color = Color.White
            )
        }
    }
}

@Composable
@Preview(device = "id:wearos_small_round", showSystemUi = true)
fun GameActionButtonPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        GameActionButton(
            title = "Reset Game",
            icon = Icons.Rounded.Star,
            action = {},
            iconColor = WearColors.White,
            backgroundColor = WearColors.DismissRed
        )
    }
}