package com.example.namibiahockeyapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.namibiahockeyapp.navigation.Screen
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun TeamRegistrationScreen(navController: NavController) {
    var teamName by remember { mutableStateOf("") }
    var coachName by remember { mutableStateOf("") }
    val context = LocalContext.current

    val db = FirebaseFirestore.getInstance()

    Column(modifier = Modifier.padding(24.dp)) {

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text("Register Team", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = teamName, onValueChange = { teamName = it }, label = { Text("Team Name") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = coachName, onValueChange = { coachName = it }, label = { Text("Coach Name") })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val team = hashMapOf(
                "teamName" to teamName,
                "coachName" to coachName
            )
            db.collection("teams")
                .add(team)
                .addOnSuccessListener {
                    Toast.makeText(context, "Team Registered", Toast.LENGTH_SHORT).show()
                    teamName = ""
                    coachName = ""

                    navController.navigate("home") {
                        popUpTo("team_registration") { inclusive = true }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        })


        {
            Text("Submit")
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { navController.navigate(Screen.PlayerRegistration.route) }) {
                Text("Players")
            }
            Button(onClick = { navController.navigate(Screen.EventEntry.route) }) {
                Text("Events")
            }
            Button(onClick = { navController.navigate(Screen.Announcements.route) }) {
                Text("Updates")
            }
        }
    }
}