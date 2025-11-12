package com.micahnyabuto.snap2pdf.features.settings

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState // Added import
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // Added import
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.micahnyabuto.snap2pdf.R
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AboutDialog(onDismiss = { showDialog = false })
    }
    var showMessageDialog by remember { mutableStateOf(false) }

    if (showMessageDialog) {
        RequestFeatureDialog(onDismiss = { showMessageDialog = false })
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SettingCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Enable Dark Theme")
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.toggleTheme(it) }
                    )
                }
            }

            SettingCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Rate us")
                    IconButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Coming soon",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rate us"
                        )
                    }
                }
            }
            SettingCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Share app with friends")
                    IconButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Coming soon",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share app"
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
                    .clickable{
                        showDialog = true
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("About Snap2PDF")
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About"
                    )

                }
            }
        }
            Card(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://snap2pdf-privacy.netlify.app/".toUri()
                        )
                        context.startActivity(intent)
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Privacy Policy")
                            Icon(
                                imageVector = Icons.Default.PrivacyTip,
                                contentDescription = "Privacy"
                            )

                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Our terms and conditions",
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth()
                    .clickable{
                        showMessageDialog = true
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ){

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Request a feature")
                    Icon(
                        imageVector = Icons.Default.NavigateNext,
                        contentDescription = "Privacy"
                    )

                }
            }
        }
            SettingCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                   Text("How to reach Us")
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                                           .padding(start =8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://x.com/Snap_2PDF?t=y67fbcpCWyz45yf_Z_w_Lg&s=09".toUri()
                            )
                            context.startActivity(intent)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.twitter),
                                contentDescription = "Twitter",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)

                            )
                        }
                        Text("Follow us on X")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                                           .padding(start =8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        ) {
                        IconButton(onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "mailto:atembamicah@gmail.com".toUri()
                            )
                            context.startActivity(intent)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                modifier = Modifier.size(24.dp)

                            )
                        }
                        Text("Developer")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            content()
        }
    }
}
@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.applogo),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Snap2PDF",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Text(
                text = "Snap2PDF makes it simple to turn your images into professional PDF files,Your pocket friendly Image -> PDF converter.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
fun RequestFeatureDialog(
    onDismiss: () -> Unit,
    phoneNumber: String = "+254769782503"
){
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }


    if (isLoading) {
        LaunchedEffect(Unit) {
            delay(1000)
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://wa.me/${phoneNumber.removePrefix("+")}?text=${Uri.encode(message)}".toUri()
            )
            context.startActivity(intent)
            isLoading = false
            onDismiss()
        }
    }

    AlertDialog(
        onDismissRequest = { 
            if (!isLoading) {
                onDismiss() 
            }
        },
        title = {
            Text("Send Message", style = MaterialTheme.typography.titleMedium)
        },
        text = {
            OutlinedTextField(
                value = message,
                onValueChange = {message = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = {Text("Type your message...", style = MaterialTheme.typography.bodyMedium)},
                textStyle = TextStyle(
                    fontSize = 14.sp
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                    disabledBorderColor = Color.Transparent
                ),
                enabled = !isLoading
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { 
                        if (!isLoading) onDismiss() 
                    },
                    enabled = !isLoading
                ) {
                    Text("Cancel")
                }
                Spacer(Modifier.width(8.dp))

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Button(
                        onClick = {
                            if (message.isNotBlank()) {
                                isLoading = true
                            }
                        },
                        enabled = message.isNotBlank()
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    )
}
