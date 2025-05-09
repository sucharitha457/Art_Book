package com.example.canvas.presentation.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.canvas.R
import com.example.canvas.data.local.ArtEntity
import com.example.canvas.data.local.PathCommand
import com.example.canvas.data.local.toDrawingPath
import com.example.canvas.data.local.toSerializable
import com.example.canvas.presentation.viewmodel.DetailViewmodel
import com.example.canvas.ui.theme.Primary

val TAG = "DetailScreen"
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    artId: Int?,
    viewmodel: DetailViewmodel = hiltViewModel()
) {
    val paths = remember { mutableStateListOf<DrawingPath>() }
    var currentPath by remember { mutableStateOf(Path()) }
    val selectedColor = remember { mutableStateOf(Color.Black) }
    val showColorPicker = remember { mutableStateOf(false) }
    val strokeWidth = remember { mutableStateOf(4f) }
    var title by remember { mutableStateOf("Untitled") }
    var isEditable by remember { mutableStateOf(true) }

    val redrawTrigger = remember { mutableStateOf(0) } // force recompose Canvas

    if (showColorPicker.value) {
        CustomColorPickerDialog(
            onColorSelected = {
                selectedColor.value = it
            },
            onDismiss = { showColorPicker.value = false }
        )
    }

    LaunchedEffect(artId) {
        if (artId != null) {
            val art = viewmodel.getNote(artId)
            if (art != null) {
                isEditable = false
                title = art.title
                paths.clear()
                paths.addAll(art.data.map { it.toDrawingPath() })
                redrawTrigger.value++
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(10.dp))
            BasicTextField(
                value = title,
                enabled = isEditable,
                onValueChange = { title = it },
                textStyle = TextStyle(color = Color.Black, fontSize = 26.sp),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
            )
            if (isEditable) {
                Button(
                    modifier = Modifier.align(Alignment.CenterVertically).defaultMinSize(minHeight = 40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                    val serializablePaths = paths.map { it.toSerializable() }
                    val entity = ArtEntity(
                        id = artId ?: 0,
                        title = title,
                        createdTime = System.currentTimeMillis() / 1000,
                        data = serializablePaths
                    )
                    if(title != ""){
                        if (artId == null) viewmodel.saveArt(entity)
                        else viewmodel.updateNote(entity)
                    }else{
                        Log.d(TAG, "DetailScreen: title is empty")
                    }
                    isEditable = false
                }) {
                    Text("Save")
                }
            } else {
                IconButton(onClick = { isEditable = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }

            if (artId != null) {
                IconButton(onClick = {
                    viewmodel.deleteNote(artId)
                    navController.navigate(NavItem.Home.route)
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
        ) {
            val commandList = remember { mutableListOf<PathCommand>() }

            var canvasSize by remember { mutableStateOf(IntSize.Zero) }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { canvasSize = it }
                    .pointerInput(isEditable) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                if (!isEditable || !offset.inside(canvasSize)) return@detectDragGestures
                                currentPath = Path().apply { moveTo(offset.x, offset.y) }
                                commandList.clear()
                                commandList.add(PathCommand.MoveTo(offset.x, offset.y))
                            },
                            onDrag = { change, _ ->
                                val pos = change.position
                                if (!isEditable || !pos.inside(canvasSize)) return@detectDragGestures
                                currentPath.lineTo(pos.x, pos.y)
                                redrawTrigger.value++
                                commandList.add(PathCommand.LineTo(pos.x, pos.y))
                            },
                            onDragEnd = {
                                if (!isEditable) return@detectDragGestures
                                if (commandList.isNotEmpty()) {
                                    paths.add(
                                        DrawingPath(
                                            path = currentPath,
                                            color = selectedColor.value,
                                            strokeWidth = strokeWidth.value,
                                            commands = commandList.toList()
                                        )
                                    )
                                }
                                currentPath = Path()
                                commandList.clear()
                            }
                        )
                    }
            ) {
                redrawTrigger.value
                paths.forEach { path ->
                    drawPath(
                        path = path.path,
                        color = path.color,
                        style = Stroke(width = path.strokeWidth)
                    )
                }

                drawPath(
                    path = currentPath,
                    color = selectedColor.value,
                    style = Stroke(width = strokeWidth.value)
                )
            }
        }

        if (isEditable) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier.align(Alignment.CenterVertically).defaultMinSize(minHeight = 40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { paths.clear() })
                {
                    Text("Clear")
                }
                IconButton(onClick = { paths.removeLastOrNull() }) {
                    Icon(painterResource(id = R.drawable.undo), contentDescription = "Undo")
                }
                Image(
                    painter = painterResource(R.mipmap.color_wheel_foreground),
                    contentDescription = "Color Picker",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { showColorPicker.value = true }
                )
                Slider(
                    value = strokeWidth.value,
                    onValueChange = { strokeWidth.value = it },
                    valueRange = 1f..40f,
                    colors = SliderColors(
                        thumbColor = Primary,
                        activeTrackColor = Primary,
                        activeTickColor = Primary,
                        inactiveTrackColor = Primary,
                        inactiveTickColor = Primary,
                        disabledThumbColor = Primary,
                        disabledActiveTrackColor = Primary,
                        disabledActiveTickColor = Primary,
                        disabledInactiveTrackColor = Primary,
                        disabledInactiveTickColor = Primary
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text("${strokeWidth.value.toInt()}")
            }
        }
    }
}

private fun Offset.inside(size: IntSize): Boolean {
    return x in 0f..size.width.toFloat() && y in 0f..size.height.toFloat()
}



data class DrawingPath(
    val path: Path,
    val color: Color,
    val strokeWidth: Float = 4f,
    val commands: List<PathCommand>
)
