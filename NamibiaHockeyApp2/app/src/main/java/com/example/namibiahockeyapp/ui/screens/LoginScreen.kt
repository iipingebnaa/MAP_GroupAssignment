package com.example.namibiahockeyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.namibiahockeyapp.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    // List of users fetched from Firestore
    val registeredUsers = remember { mutableStateListOf<Map<String, String>>() }
    var showUsers by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text("Welcome Back!", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Icon(
                        imageVector = if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            singleLine = true,
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                errorMessage.value = ""
                val trimmedEmail = email.value.trim()

                if (trimmedEmail.isBlank() || password.value.isBlank()) {
                    errorMessage.value = "Please enter both email and password"
                    return@Button
                }

                isLoading.value = true

                auth.signInWithEmailAndPassword(trimmedEmail, password.value)
                    .addOnCompleteListener { task ->
                        isLoading.value = false
                        if (task.isSuccessful) {
                            errorMessage.value = ""
                            onLoginSuccess()  // <--- THIS IS THE FIX, call on success

                            showUsers = true

                            // Fetch registered users from Firestore
                            firestore.collection("users")
                                .get()
                                .addOnSuccessListener { result ->
                                    registeredUsers.clear()
                                    for (doc in result) {
                                        val userMap = mapOf(
                                            "username" to (doc.getString("username") ?: ""),
                                            "email" to (doc.getString("email") ?: "")
                                        )
                                        registeredUsers.add(userMap)
                                    }
                                }
                                .addOnFailureListener { e ->
                                    errorMessage.value = "Failed to fetch users: ${e.message}"
                                }
                        } else {
                            errorMessage.value = task.exception?.message ?: "Login failed"
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E4F33))
        ) {
            if (isLoading.value) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login", color = Color.White)
            }
        }

        if (errorMessage.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage.value, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Don't have an account? Register here.",
            color = Color(0xFF2E4F33),
            modifier = Modifier.clickable {
                navController.navigate(Screen.Registration.route)
            },
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (showUsers) {
            Text("Registered Users:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(registeredUsers) { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Text("Username: ${user["username"]}", fontWeight = FontWeight.SemiBold)
                        Text("Email: ${user["email"]}")
                    }
                    Divider()
                }
            }
        }
    }
}
