package com.example.canvas.presentation.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.canvas.data.local.ArtEntity
import com.example.canvas.presentation.viewmodel.HomeViewmodel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(modifier: Modifier = Modifier , viewmodel: HomeViewmodel = hiltViewModel(), navController: NavController) {
    val arts by viewmodel.getArts().collectAsState(initial = emptyList())
    Column {
        Text(
            text = "Arts",
            fontSize = 24.sp,
            color = androidx.compose.ui.graphics.Color.Black,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 16.dp)
        )
        Box {
            displayArts(arts, navController)
            FloatingActionButton(
                onClick = { navController.navigate(NavItem.Art.route) },
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.BottomEnd),
                containerColor = androidx.compose.ui.graphics.Color.Black
            ) {
                Text("+", fontSize = 30.sp, color = androidx.compose.ui.graphics.Color.White)
            }
        }
    }
}

@Composable
fun displayArts(arts: List<ArtEntity>, navController: NavController) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        var leftHeight = 0
        var rightHeight = 0

        items(arts.size) { index ->
            val art = arts[index]
            val fullWidth = (leftHeight == rightHeight)

            artCard(
                art = art,
                navController = navController,
                fullWidth = fullWidth
            )

            val randomHeight = art.title.length % 3 + 1
            if (fullWidth) {
                leftHeight += randomHeight
                rightHeight += randomHeight
            } else if (leftHeight <= rightHeight) {
                leftHeight += randomHeight
            } else {
                rightHeight += randomHeight
            }
        }
    }
}

@Composable
fun artCard(art: ArtEntity, navController: NavController, fullWidth: Boolean) {
    val noteColors = listOf(
        androidx.compose.ui.graphics.Color(0xFFFFF176),
        androidx.compose.ui.graphics.Color(0xFF81C784),
        androidx.compose.ui.graphics.Color(0xFF64B5F6),
        androidx.compose.ui.graphics.Color(0xFFBA68C8),
        androidx.compose.ui.graphics.Color(0xFFFF8A65),
        androidx.compose.ui.graphics.Color(0xFFFFA726),
        androidx.compose.ui.graphics.Color(0xFFA1887F),
        androidx.compose.ui.graphics.Color(0xFF4DD0E1),
    )
    val backgroundColor = noteColors[art.id % noteColors.size]

    Card(
        modifier = Modifier
            .then(
                if (fullWidth) Modifier.fillMaxWidth()
                else Modifier.fillMaxWidth(0.5f)
            )
            .wrapContentHeight()
            .clickable {
                Log.d("HomeScreen", "Clicked on art with ID: ${art.id}")
                navController.navigate(NavItem.Art.withNoteId(art.id))
            },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = art.title,
                fontSize = 18.sp,
                color = androidx.compose.ui.graphics.Color.Black
            )
            Text(
                text = convertUnixToDate(art.createdTime),
                fontSize = 14.sp,
                color = androidx.compose.ui.graphics.Color.DarkGray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

fun convertUnixToDate(unixTime: Long): String {
    val date = Date(unixTime * 1000)
    val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}
