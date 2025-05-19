package com.example.namibiahockeyapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun fetchPlayers(): List<Player> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("players").get().await()
        snapshot.documents.mapNotNull { it.toObject(Player::class.java) }
    } catch (e: Exception) {
        emptyList()
    }
}
