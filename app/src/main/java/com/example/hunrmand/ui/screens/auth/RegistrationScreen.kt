package com.example.hunrmand.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.hunrmand.domain.model.UserRole
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegistrationScreen(
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.USER) }
    
    // Worker specific fields
    var profession by remember { mutableStateOf("") }
    var hourlyRate by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState?.isSuccess == true) {
            onRegistrationSuccess()
            viewModel.resetAuthState()
        }
    }

    val lightOrange = Color(0xFFFFE0B2) 
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
             Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Person, 
                contentDescription = "Logo",
                tint = primaryOrange,
                modifier = Modifier
                    .size(60.dp)
                    .padding(bottom = 16.dp)
            )
            
            Text("Create Account", style = MaterialTheme.typography.headlineMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                     focusedBorderColor = primaryOrange,
                     unfocusedContainerColor = lightOrange.copy(alpha = 0.3f),
                     focusedContainerColor = lightOrange.copy(alpha = 0.3f)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
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
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                     focusedBorderColor = primaryOrange,
                     unfocusedContainerColor = lightOrange.copy(alpha = 0.3f),
                     focusedContainerColor = lightOrange.copy(alpha = 0.3f)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Select Role:", style = MaterialTheme.typography.titleMedium)
            Row {
                UserRole.entries.forEach { role ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (role == selectedRole),
                            onClick = { selectedRole = role },
                            colors = RadioButtonDefaults.colors(selectedColor = primaryOrange)
                        )
                        Text(role.name)
                    }
                }
            }
            
            if (selectedRole == UserRole.WORKER) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = profession,
                    onValueChange = { profession = it },
                    label = { Text("Profession") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                     colors = OutlinedTextFieldDefaults.colors(
                         focusedBorderColor = primaryOrange,
                         unfocusedContainerColor = lightOrange.copy(alpha = 0.3f),
                         focusedContainerColor = lightOrange.copy(alpha = 0.3f)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = hourlyRate,
                    onValueChange = { hourlyRate = it },
                    label = { Text("Hourly Rate") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                     colors = OutlinedTextFieldDefaults.colors(
                         focusedBorderColor = primaryOrange,
                         unfocusedContainerColor = lightOrange.copy(alpha = 0.3f),
                         focusedContainerColor = lightOrange.copy(alpha = 0.3f)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { 
                    viewModel.register(
                        name, 
                        email, 
                        password, 
                        selectedRole,
                        if (selectedRole == UserRole.WORKER) profession else null,
                        if (selectedRole == UserRole.WORKER) hourlyRate else null
                    ) 
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp)
            ) {
                Text("Register", style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login", color = primaryOrange)
            }
            
            if (authState?.isFailure == true) {
                Text(
                    text = "Error: ${authState?.exceptionOrNull()?.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
             Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
