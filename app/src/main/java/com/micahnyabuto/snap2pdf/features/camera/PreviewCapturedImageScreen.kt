package com.micahnyabuto.snap2pdf.features.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewCapturedImageScreen(uri: Uri, navController: NavController) {

    val context = LocalContext.current
    var rotationDegrees by remember { mutableFloatStateOf(0f) }
    var currentUri by remember { mutableStateOf(uri) }

    // ✅ CanHub Cropper Launcher
    val cropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            val uriContent = result.uriContent
            if (uriContent != null) {
                currentUri = uriContent
            }
        } else {
            val exception = result.error
            exception?.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Image") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIos, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            CropBottomBar(
                onRotateLeft = { rotationDegrees -= 90f },
                onRotateRight = { rotationDegrees += 90f },
                onSelectCrop = {
                    cropLauncher.launch(
                        CropImageContractOptions(
                            uri = currentUri,
                            cropImageOptions = CropImageOptions().apply {
                                aspectRatioX = 1
                                aspectRatioY = 1
                                fixAspectRatio = true
                                outputCompressQuality = 90
                            }
                        )
                    )
                },
                onNext = {
                    // TODO: Implement your NEXT action here
                }
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
                model = currentUri,
                contentDescription = "Preview Image",
                modifier = Modifier.graphicsLayer(
                    rotationZ = rotationDegrees
                )
            )
        }
    }
}

@Composable
fun CropBottomBar(
    onRotateLeft: () -> Unit,
    onRotateRight: () -> Unit,
    onSelectCrop: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 60.dp),
        horizontalArrangement = Arrangement.SpaceAround,
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
            icon = Icons.Default.Crop,
            label = "Crop",
            onClick = onSelectCrop
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
