package com.example.hunrmand.presentation.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hunrmand.data.source.local.entity.AddressEntity
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressListScreen(
    navController: NavController,
    viewModel: AddressViewModel = koinViewModel()
) {
    val addresses by viewModel.addresses.collectAsState()
    
    // Dialog State
    var showDialog by remember { mutableStateOf(false) }
    var currentAddress by remember { mutableStateOf<AddressEntity?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Addresses") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                currentAddress = null
                showDialog = true 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Address")
            }
        }
    ) { paddingValues ->
        if (addresses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No addresses found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(addresses) { address ->
                    AddressItem(
                        address = address,
                        onDelete = { viewModel.deleteAddress(address) },
                        onEdit = {
                            currentAddress = address
                            showDialog = true
                        },
                        onSetDefault = { viewModel.setDefault(address) }
                    )
                }
            }
        }
    }
    
    if (showDialog) {
        AddressDialog(
            address = currentAddress,
            onDismiss = { showDialog = false },
            onConfirm = { label, fullAddress, lat, lng, isDefault ->
                if (currentAddress == null) {
                    viewModel.addAddress(label, fullAddress, lat, lng, isDefault)
                } else {
                    viewModel.updateAddress(currentAddress!!.copy(
                        label = label,
                        fullAddress = fullAddress,
                        latitude = lat,
                        longitude = lng, 
                        isDefault = isDefault
                    ))
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun AddressItem(
    address: AddressEntity,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onSetDefault: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (address.isDefault) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = address.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (address.isDefault) {
                    Text(
                        text = "Default",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = address.fullAddress,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (!address.isDefault) {
                    TextButton(onClick = onSetDefault) {
                        Text("Set Default")
                    }
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun AddressDialog(
    address: AddressEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double, Double, Boolean) -> Unit
) {
    var label by remember { mutableStateOf(address?.label ?: "") }
    var fullAddress by remember { mutableStateOf(address?.fullAddress ?: "") }
    var isDefault by remember { mutableStateOf(address?.isDefault ?: false) }
    
    // Mock lat/long for now as we don't have a map picker here yet
    // In real app, launch LocationPicker
    val lat = 0.0 
    val lng = 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (address == null) "Add Address" else "Edit Address") },
        text = {
            Column {
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label (e.g., Home)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fullAddress,
                    onValueChange = { fullAddress = it },
                    label = { Text("Full Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isDefault, onCheckedChange = { isDefault = it })
                    Text("Set as Default")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(label, fullAddress, lat, lng, isDefault) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
