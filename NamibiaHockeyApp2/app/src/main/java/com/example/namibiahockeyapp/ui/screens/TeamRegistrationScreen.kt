package com.example.namibiahockeyapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamRegistrationScreen(navController: NavController) {
    val greenColor = Color(0xFF3C5B46)

    var teamName by remember { mutableStateOf("") }
    var teamCoach by remember { mutableStateOf("") }

    // Image Picker for Team Logo
    var logoUri by remember { mutableStateOf<Uri?>(null) }
    val logoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        logoUri = it
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Team Registration", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Text("Team Name")
                OutlinedTextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text("Team Coach")
                OutlinedTextField(
                    value = teamCoach,
                    onValueChange = { teamCoach = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text("Team Logo")
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (logoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(logoUri),
                            contentDescription = "Team Logo",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    } else {
                        Text("No Logo Selected", color = Color.Gray)
                    }
                }
                Button(onClick = { logoPickerLauncher.launch("image/*") }) {
                    Text("Upload Logo")
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // TODO: Handle Team Registration logic
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register Team", color = Color.White)
                }
            }
        }
    }
}
