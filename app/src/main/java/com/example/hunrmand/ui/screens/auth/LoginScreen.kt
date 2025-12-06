package com.example.hunrmand.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(loginState, currentUser) {
        if (loginState?.isSuccess == true || currentUser != null) {
            onLoginSuccess()
            viewModel.resetAuthState()
        }
    }

    // Colors
    val lightOrange = Color(0xFFFFE0B2) // Peach/Orange light
    val primaryOrange = Color(0xFFFF9800) 
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color.White, lightOrange)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo placeholder (Icon)
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Home, // Placeholder for logo
                contentDescription = "Logo",
                tint = primaryOrange,
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            
            Text(
                text = "Login to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryOrange,
                    unfocusedContainerColor = lightOrange.copy(alpha = 0.3f),
                    focusedContainerColor = lightOrange.copy(alpha = 0.3f)
                ),
                leadingIcon = { 
                    Icon(androidx.compose.material.icons.Icons.Default.Email, contentDescription = null) 
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryOrange,
                    unfocusedContainerColor = lightOrange.copy(alpha = 0.3f),
                    focusedContainerColor = lightOrange.copy(alpha = 0.3f)
                ),
                leadingIcon = { 
                    Icon(androidx.compose.material.icons.Icons.Default.Lock, contentDescription = null) 
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp)
            ) {
                Text("Login", style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Sign up", color = primaryOrange)
            }
            
            if (loginState?.isFailure == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Error: ${loginState?.exceptionOrNull()?.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
