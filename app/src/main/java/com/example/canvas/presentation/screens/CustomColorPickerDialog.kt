package com.example.canvas.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomColorPickerDialog(
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val themeColors = listOf(
        listOf(
            Color(0xFF818080),
            Color(0xFFa6a6a5),
            Color(0xFFbfbfbf),
            Color(0xFFd9d9d9),
            Color(0xFFf2f2f3)
        ),
        listOf(
            Color(0xFF0d0d0e),
            Color(0xFF262626),
            Color(0xFF404140),
            Color(0xFF595958),
            Color(0xFF80807f)
        ),
        listOf(
            Color(0xFFf62c01),
            Color(0xFFFF6241),
            Color(0xFFfe8b73),
            Color(0xFFffb4a2),
            Color(0xFFffb4a2)
        ),
        listOf(
            Color(0xFF151921),
            Color(0xFF1e2730),
            Color(0xFF6a7ea1),
            Color(0xFF9caac0),
            Color(0xFFcdd3e0)
        ),
        listOf(
            Color(0xFF7f3200),
            Color(0xFFbf4d01),
            Color(0xFFffa467),
            Color(0xFFffc29a),
            Color(0xFFffdfcd)
        ),
        listOf(
            Color(0xFF18373b),
            Color(0xFF235357),
            Color(0xFF71bbc0),
            Color(0xFF71bbc0),
            Color(0xFF71bbc0)
        ),
        listOf(
            Color(0xFF801211),
            Color(0xFFbf1a1b),
            Color(0xFFef8b8c),
            Color(0xFFf6b3b1),
            Color(0xFFfad8d8)
        ),
        listOf(
            Color(0xFF8d620f),
            Color(0xFFd59312),
            Color(0xFFf5d392),
            Color(0xFFf8e0b6),
            Color(0xFFfcf0db)
        ),
    )

    val standardColors = listOf(
        Color(0xFFc00000),
        Color(0xFFff0000),
        Color(0xFFfec103),
        Color(0xFFffff05),
        Color(0xFF93d052),
        Color(0xFF00b050),
        Color(0xFF00b0f0),
        Color(0xFF016fc0),
        Color(0xFF016fc0),
        Color(0xFF7030a0)
    )

    var selectedColor by remember { mutableStateOf(Color.Black) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Pick a Color")
            }
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Theme Colors")
                themeColors.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        row.forEach { color ->
                            Color(color) {
                                selectedColor = it
                                onColorSelected(it)
                                onDismiss()
                            }
                        }
                    }
                }

                Text("Standard Colors", modifier = Modifier.padding(top = 12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    standardColors.forEach { color ->
                        Color(color) {
                            selectedColor = it
                            onColorSelected(it)
                            onDismiss()
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
fun Color(color: Color, onClick: (Color) -> Unit) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .clickable { onClick(color) }
    )
}