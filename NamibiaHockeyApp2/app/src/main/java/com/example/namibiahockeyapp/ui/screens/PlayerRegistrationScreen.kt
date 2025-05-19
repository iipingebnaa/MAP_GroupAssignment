package com.example.namibiahockeyapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerRegistrationScreen(navController: NavController) {
    val greenColor = Color(0xFF3C5B46)

    // State variables
    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var postalAddress by remember { mutableStateOf("") }
    var residentialAddress by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var shootCatch by remember { mutableStateOf("") }

    var guardianName by remember { mutableStateOf("") }
    var guardianId by remember { mutableStateOf("") }
    var guardianEmail by remember { mutableStateOf("") }
    var guardianPhone by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player Registration", color = Color.White) },
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("Player's Full Name")
                OutlinedTextField(value = fullName, onValueChange = { fullName = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Birthdate (DD/MM/YYYY)")
                OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Email Address")
                OutlinedTextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Cellphone Number")
                OutlinedTextField(value = phone, onValueChange = { phone = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Identity/Passport Number")
                OutlinedTextField(value = idNumber, onValueChange = { idNumber = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Postal Address")
                OutlinedTextField(value = postalAddress, onValueChange = { postalAddress = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Residential Address")
                OutlinedTextField(value = residentialAddress, onValueChange = { residentialAddress = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Position")
                val positions = listOf("Forward", "Defense", "Forward or Defense", "Goalie")
                positions.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (position == it),
                            onClick = { position = it },
                            colors = RadioButtonDefaults.colors(selectedColor = greenColor)
                        )
                        Text(it)
                    }
                }
            }

            item {
                Text("Shoot/Catch")
                val options = listOf("Right", "Left")
                options.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (shootCatch == it),
                            onClick = { shootCatch = it },
                            colors = RadioButtonDefaults.colors(selectedColor = greenColor)
                        )
                        Text(it)
                    }
                }
            }

            item {
                Text("Guardian Full Name")
                OutlinedTextField(value = guardianName, onValueChange = { guardianName = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Guardian Identity Number")
                OutlinedTextField(value = guardianId, onValueChange = { guardianId = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Guardian Email Address")
                OutlinedTextField(value = guardianEmail, onValueChange = { guardianEmail = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Text("Guardian Cellphone Number")
                OutlinedTextField(value = guardianPhone, onValueChange = { guardianPhone = it }, modifier = Modifier.fillMaxWidth())
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Handle form submission
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit", color = Color.White)
                }
            }
        }
    }
}
