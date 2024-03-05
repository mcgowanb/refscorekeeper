package com.mcgowanb.projects.refereescorekeeper.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoreBox(
    modifier: Modifier,
    onDoubleClick: () -> Unit,
    onLongClick: () -> Unit,
    score: String,
    textAlign: TextAlign,
    textModifier: Modifier
) {
    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                },
                onLongClick = {
                    onLongClick()
                },
                onDoubleClick = {
                    onDoubleClick()
                }
            )
            .then(modifier)
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .then(textModifier),
            textAlign = textAlign,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            text = score
        )
    }

}