package com.mcgowanb.projects.refereescorekeeper.ui.input

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.mcgowanb.projects.refereescorekeeper.const.WearColors
import com.mcgowanb.projects.refereescorekeeper.enums.Direction
import com.mcgowanb.projects.refereescorekeeper.ui.animation.SlideLeftToRight
import com.mcgowanb.projects.refereescorekeeper.utility.VibrationUtility

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MinutePicker(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    initialValue: Int,
    range: IntRange,
    vibrationUtility: VibrationUtility,
    title: String,
    visible: Boolean
) {
    var currentValue by remember(initialValue, range) {
        mutableIntStateOf(initialValue.coerceIn(range))
    }

    fun changeMinutes(direction: Direction) {
        currentValue = when (direction) {
            Direction.INCREMENT -> when {
                currentValue < range.last -> currentValue + 1
                else -> range.first
            }

            Direction.DECREMENT -> when {
                currentValue > range.first -> currentValue - 1
                else -> range.last
            }
        }
        vibrationUtility.click()
    }

    SlideLeftToRight(visible = visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 0.dp)
                ) {
                    Chip(
                        onClick = { changeMinutes(Direction.DECREMENT) },
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        label = { Text("") },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "Decrease",
                                tint = WearColors.Pink,
                                modifier = Modifier.size(45.dp)
                            )
                        }
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentValue.toString().padStart(2, '0'),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = WearColors.White
                        )
                    }

                    Chip(
                        onClick = { changeMinutes(Direction.INCREMENT) },
                        colors = ChipDefaults.chipColors(backgroundColor = Color.Transparent),
                        label = { Text("") },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowUp,
                                contentDescription = "Increase",
                                tint = WearColors.Pink,
                                modifier = Modifier.size(55.dp)
                            )
                        }
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
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
                            contentDescription = null
                        )
                    }
                    Button(
                        onClick = { onConfirm(currentValue) },
                        modifier = Modifier.size(ButtonDefaults.DefaultButtonSize),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = WearColors.ConfirmGreen,
                            contentColor = WearColors.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Done,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(device = "id:wearos_small_round", showSystemUi = true)
@Composable
fun MinutePickerPreview() {
    MinutePicker(
        onConfirm = {},
        onDismiss = {},
        initialValue = 22,
        range = 1..30,
        vibrationUtility = VibrationUtility(null),
        title = "Mins",
        visible = true
    )
}