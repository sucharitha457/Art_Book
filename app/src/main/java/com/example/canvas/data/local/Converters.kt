package com.example.canvas.data.local

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.room.TypeConverter
import com.example.canvas.presentation.screens.DrawingPath
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

sealed class PathCommand {
    data class MoveTo(val x: Float, val y: Float) : PathCommand()
    data class LineTo(val x: Float, val y: Float) : PathCommand()
}

class PathCommandAdapter {
    @ToJson
    fun toJson(pathCommand: PathCommand): Map<String, Any> {
        return when (pathCommand) {
            is PathCommand.LineTo -> mapOf("type" to "LineTo", "x" to pathCommand.x, "y" to pathCommand.y)
            is PathCommand.MoveTo -> mapOf("type" to "MoveTo", "x" to pathCommand.x, "y" to pathCommand.y)
        }
    }

    @FromJson
    fun fromJson(json: Map<String, Any>): PathCommand {
        val type = json["type"] as? String ?: throw IllegalArgumentException("Missing or invalid type in JSON")
        return when (type) {
            "MoveTo" -> {
                val x = (json["x"] as? Number)?.toFloat() ?: 0f
                val y = (json["y"] as? Number)?.toFloat() ?: 0f
                PathCommand.MoveTo(x, y)
            }
            "LineTo" -> {
                val x = (json["x"] as? Number)?.toFloat() ?: 0f
                val y = (json["y"] as? Number)?.toFloat() ?: 0f
                PathCommand.LineTo(x, y)
            }
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}

class Converters {
    private val moshi = Moshi.Builder()
        .add(PathCommandAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromSerializableDrawingPathList(value: List<SerializableDrawingPath>): String {
        val type = Types.newParameterizedType(List::class.java, SerializableDrawingPath::class.java)
        val jsonAdapter = moshi.adapter<List<SerializableDrawingPath>>(type)
        return jsonAdapter.toJson(value)
    }

    @TypeConverter
    fun toSerializableDrawingPathList(value: String): List<SerializableDrawingPath> {
        val type = Types.newParameterizedType(List::class.java, SerializableDrawingPath::class.java)
        val jsonAdapter = moshi.adapter<List<SerializableDrawingPath>>(type)
        return jsonAdapter.fromJson(value) ?: emptyList()
    }
}

data class SerializableDrawingPath(
    val commands: List<PathCommand> = emptyList(),
    val color: Long,
    val strokeWidth: Float
)

fun DrawingPath.toSerializable(): SerializableDrawingPath {
    return SerializableDrawingPath(
        commands = commands,
        color = color.value.toLong(),
        strokeWidth = strokeWidth
    )
}
fun SerializableDrawingPath.toDrawingPath(): DrawingPath {
    val path = Path()
    for (command in commands) {
        when (command) {
            is PathCommand.MoveTo -> path.moveTo(command.x, command.y)
            is PathCommand.LineTo -> path.lineTo(command.x, command.y)
        }
    }
    return DrawingPath(
        path = path,
        color = Color(color.toULong()),
        strokeWidth = strokeWidth,
        commands = commands
    )
}
