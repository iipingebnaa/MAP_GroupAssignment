package com.example.namibiahockeyapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PlayerRegistrationScreen(navController: NavController) {
    var playerName by remember { mutableStateOf("") }
    var teamName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    Column(modifier = Modifier.padding(24.dp)) {

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text("Register Player", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = playerName, onValueChange = { playerName = it }, label = { Text("Player Name") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = teamName, onValueChange = { teamName = it }, label = { Text("Team Name") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = position, onValueChange = { position = it }, label = { Text("Position") })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val player = hashMapOf(
                "playerName" to playerName,
                "teamName" to teamName,
                "position" to position
            )
            db.collection("players")
                .add(player)
                .addOnSuccessListener {
                    Toast.makeText(context, "Player Registered", Toast.LENGTH_SHORT).show()
                    playerName = ""
                    teamName = ""
                    position = ""
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Register Player")
        }
    }
}
