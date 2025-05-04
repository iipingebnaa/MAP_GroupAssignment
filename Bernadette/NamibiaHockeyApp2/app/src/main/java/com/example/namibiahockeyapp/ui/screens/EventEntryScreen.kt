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
fun EventEntryScreen(navController: NavController) {
    var eventName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    Column(modifier = Modifier.padding(24.dp)) {

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text("Create Event", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = eventName, onValueChange = { eventName = it }, label = { Text("Event Name") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (e.g. 2025-06-15)") })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val event = hashMapOf(
                "eventName" to eventName,
                "location" to location,
                "date" to date
            )
            db.collection("events")
                .add(event)
                .addOnSuccessListener {
                    Toast.makeText(context, "Event Created", Toast.LENGTH_SHORT).show()
                    eventName = ""
                    location = ""
                    date = ""
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Submit Event")
        }
    }
}
