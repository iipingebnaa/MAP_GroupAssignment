package com.example.namibiahockeyapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.*

@Composable
fun AnnouncementsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var announcements by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        db.collection("announcements")
            .addSnapshotListener { snapshot, _ ->
                announcements = snapshot?.documents?.mapNotNull {
                    it.getString("message")
                } ?: emptyList()
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text("Announcements", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(announcements) { msg ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)) {
                    Text(msg, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
