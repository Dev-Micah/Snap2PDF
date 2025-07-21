package com.micahnyabuto.snap2pdf.features.camera

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewCapturedImageScreen(uri: Uri, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crop") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIos, contentDescription = "Close")
                    }
                }
            )
        },
        bottomBar = {

            CropBottomBar(
                onRotateLeft = { /* TODO: rotate left */ },
                onRotateRight = { /* TODO: rotate right */ },
                onSelectAll = { /* TODO: select all */ },
                onNext = { /* TODO: next action */ },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = uri,
                contentDescription = "Captured Image"
            )


        }


    }
}

@Composable
fun CropBottomBar(
    onRotateLeft: () -> Unit,
    onRotateRight: () -> Unit,
    onSelectAll: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.background(MaterialTheme.colorScheme.onSurfaceVariant)
            .padding(vertical = 8.dp, horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CropBarButton(
            icon = Icons.Default.RotateLeft,
            label = "Left",
            onClick = onRotateLeft
        )
        CropBarButton(
            icon = Icons.Default.RotateRight,
            label = "Right",
            onClick = onRotateRight
        )
        CropBarButton(
            icon = Icons.Default.CropFree,
            label = "All",
            onClick = onSelectAll
        )
        CropBarButton(
            icon = Icons.Default.ArrowForward,
            label = "Next",
            onClick = onNext
        )
    }
}

@Composable
fun CropBarButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}
