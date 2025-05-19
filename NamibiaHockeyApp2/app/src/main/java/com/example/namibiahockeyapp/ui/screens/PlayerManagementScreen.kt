package com.example.namibiahockeyapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.namibiahockeyapp.data.Player
import kotlinx.coroutines.launch
import com.example.namibiahockeyapp.data.fetchPlayers


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerManagementScreen(navController: NavController) {
    val greenColor = Color(0xFF3C5B46)
    val scope = rememberCoroutineScope()
    var players by remember { mutableStateOf<List<Player>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch {
            players = fetchPlayers()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player Management", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            items(players) { player ->
                PlayerCard(player = player, navController = navController)
            }
        }
    }
}

@Composable
fun PlayerCard(player: Player, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3C5B46))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(player.fullName, color = Color.White)
            IconButton(onClick = {
                // Navigate to player profile screen
                navController.navigate("player_profile_screen")
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "View Profile", tint = Color.White)
            }
        }
    }
}
