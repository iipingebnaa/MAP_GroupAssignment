package com.example.namibiahockeyapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEntryScreen(navController: NavController, onEventCreated: (String) -> Unit) {
    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val greenColor = Color(0xFF3C5B46)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Event", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Event Name", fontSize = 16.sp)
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                placeholder = { Text("Enter event name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Event Date", fontSize = 16.sp)
            OutlinedTextField(
                value = eventDate,
                onValueChange = { eventDate = it },
                placeholder = { Text("DD/MM/YYYY") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Location", fontSize = 16.sp)
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = { Text("Enter location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onEventCreated("Event: $eventName at $location on $eventDate")
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Event", color = Color.White)
            }
        }
    }
}