package com.llsit.joinsphere.feature.createevent

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.llsit.joinsphere.core.model.event.SelectedPlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.util.Locale

@SuppressLint("MissingPermission")
@Composable
fun LocationPickerDialog(
    onDismiss: () -> Unit,
    onLocationSelected: (SelectedPlace) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val bangkok = remember { GeoPoint(13.7563, 100.5018) }

    val mapView = remember {
        Configuration.getInstance().userAgentValue = context.packageName
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            controller.setCenter(bangkok)
        }
    }

    var selectedPlace by remember { mutableStateOf<SelectedPlace>(SelectedPlace()) }
    var isResolvingAddress by remember { mutableStateOf(false) }
    var currentCenter by remember { mutableStateOf(bangkok) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            scope.launch {
                val priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
                fusedLocationClient.getCurrentLocation(priority, CancellationTokenSource().token)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val userPoint = GeoPoint(location.latitude, location.longitude)
                            mapView.controller.animateTo(userPoint)
                            currentCenter = userPoint
                        }
                    }
            }
        }
    }

    LaunchedEffect(Unit) {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED ||
            coarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            val priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
            fusedLocationClient.getCurrentLocation(priority, CancellationTokenSource().token)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val userPoint = GeoPoint(location.latitude, location.longitude)
                        mapView.controller.setCenter(userPoint)
                        currentCenter = userPoint
                    }
                }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Lifecycle observer for MapView
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    LaunchedEffect(mapView) {
        mapView.addMapListener(DelayedMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                currentCenter = mapView.mapCenter as GeoPoint
                return true
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                currentCenter = mapView.mapCenter as GeoPoint
                return true
            }
        }, 500))
    }

    LaunchedEffect(currentCenter) {
        isResolvingAddress = true
        withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses =
                    geocoder.getFromLocation(currentCenter.latitude, currentCenter.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val addressLine = address.getAddressLine(0)
                        ?: "${currentCenter.latitude}, ${currentCenter.longitude}"
                    selectedPlace = selectedPlace.copy(
                        name = address.featureName ?: address.thoroughfare ?: addressLine,
                        address = addressLine,
                        latitude = currentCenter.latitude,
                        longitude = currentCenter.longitude
                    )
                } else {
                    selectedPlace = selectedPlace.copy(
                        name = "${currentCenter.latitude}, ${currentCenter.longitude}",
                        address = "${currentCenter.latitude}, ${currentCenter.longitude}",
                        latitude = currentCenter.latitude,
                        longitude = currentCenter.longitude
                    )
                }
            } catch (e: Exception) {
                selectedPlace = selectedPlace.copy(
                    name = "${currentCenter.latitude}, ${currentCenter.longitude}",
                    address = "${currentCenter.latitude}, ${currentCenter.longitude}",
                    latitude = currentCenter.latitude,
                    longitude = currentCenter.longitude
                )
            } finally {
                isResolvingAddress = false
            }
        }
    }

    fun searchLocation(query: String) {
        if (query.isBlank()) return
        isSearching = true
        scope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocationName(query, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val targetPoint = GeoPoint(address.latitude, address.longitude)
                    withContext(Dispatchers.Main) {
                        mapView.controller.animateTo(targetPoint)
                        currentCenter = targetPoint
                        focusManager.clearFocus()
                    }
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                withContext(Dispatchers.Main) {
                    isSearching = false
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
            )

            // Search Bar & Close Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("ค้นหาสถานที่...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = {
                                if (isSearching) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(Icons.Default.Search, contentDescription = null)
                                }
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                searchLocation(
                                    searchQuery
                                )
                            }),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, CircleShape)
                            .padding(4.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
                    }
                }
            }

            // Center Pin Indicator
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Select location",
                tint = Color(0xFF1757F0),
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
                    .padding(bottom = 20.dp)
            )

            // Current Location Button
            IconButton(
                onClick = {
                    val priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
                    fusedLocationClient.getCurrentLocation(
                        priority,
                        CancellationTokenSource().token
                    )
                        .addOnSuccessListener { location ->
                            if (location != null) {
                                val userPoint = GeoPoint(location.latitude, location.longitude)
                                mapView.controller.animateTo(userPoint)
                                currentCenter = userPoint
                            }
                        }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 220.dp, end = 16.dp)
                    .size(48.dp)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Default.MyLocation,
                    contentDescription = "My Location",
                    tint = Color(0xFF1757F0)
                )
            }

            // Bottom Confirm Panel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(24.dp)
            ) {
                Text(
                    text = "สถานที่ตั้ง (OpenStreetMap)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF737880)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    if (isResolvingAddress) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterStart),
                            color = Color(0xFF1757F0),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = selectedPlace.address,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0D0F14)
                        )
                    }
                }
                Button(
                    onClick = {
                        onLocationSelected(selectedPlace)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1757F0)),
                    enabled = !isResolvingAddress
                ) {
                    Text("ยืนยันตำแหน่ง", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
