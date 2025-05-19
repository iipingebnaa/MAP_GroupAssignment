package com.example.namibiahockeyapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.namibiahockeyapp.R
import com.example.namibiahockeyapp.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegistrationScreen(navController: NavController) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        IconButton(
            onClick = { navController.navigate(Screen.Login.route) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.Start)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Hello! Register to get started",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(12.dp))

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
                        imageVector = if (passwordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Confirm password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible.value = !confirmPasswordVisible.value }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                errorMessage.value = ""

                val trimmedEmail = email.value.trim().lowercase()

                // Validation
                when {
                    username.value.isBlank() || trimmedEmail.isBlank() || password.value.isBlank() || confirmPassword.value.isBlank() -> {
                        errorMessage.value = "Please fill all fields"
                        return@Button
                    }

                    !trimmedEmail.contains("@gmail.com") -> {
                        errorMessage.value = "Email must contain '@gmail.com'"
                        return@Button
                    }

                    password.value != confirmPassword.value -> {
                        errorMessage.value = "Passwords do not match"
                        return@Button
                    }
                }

                isLoading.value = true

                auth.createUserWithEmailAndPassword(trimmedEmail, password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                            val userMap = hashMapOf(
                                "username" to username.value,
                                "email" to trimmedEmail
                            )

                            firestore.collection("users")
                                .document(userId)
                                .set(userMap)
                                .addOnSuccessListener {
                                    isLoading.value = false
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Registration.route) { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    isLoading.value = false
                                    errorMessage.value = e.message ?: "Failed to save user data"
                                }
                        } else {
                            isLoading.value = false
                            errorMessage.value = task.exception?.message ?: "Registration failed"
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
                Text("Register", color = Color.White)
            }
        }

        if (errorMessage.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage.value, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(modifier = Modifier.padding(vertical = 8.dp))
        Text("Or Register with")
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { /* Facebook login */ }) {
                Image(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = null)
            }
            IconButton(onClick = { /* Google login */ }) {
                Image(painter = painterResource(id = R.drawable.ic_google), contentDescription = null)
            }
            IconButton(onClick = { /* Apple login */ }) {
                Image(painter = painterResource(id = R.drawable.ic_apple), contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Already have an account? ")
            Text(
                text = "Login Now",
                color = Color(0xFF2E4F33),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
