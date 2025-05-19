package com.example.namibiahockeyapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun OTPVerificationScreen(navController: NavController) {
    val otpLength = 4
    val otpDigits = remember { mutableStateListOf("", "", "", "") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "OTP Verification",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter the verification code we just sent on your email address.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until otpLength) {
                    OutlinedTextField(
                        value = otpDigits[i],
                        onValueChange = { value ->
                            if (value.length <= 1 && value.all { it.isDigit() }) {
                                otpDigits[i] = value
                            }
                        },
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val otp = otpDigits.joinToString("")
                    // Validate OTP
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C5B46))
            ) {
                Text("Verify", color = Color.White)
            }
        }

        Text(
            text = buildAnnotatedString {
                append("Didn't received code? ")
                withStyle(style = SpanStyle(color = Color(0xFF3C5B46))) {
                    append("Resend")
                }
            },
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .clickable {
                    // Handle resend
                }
        )

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
    }
}
