package com.llsit.joinsphere.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun LoginScreen(onLogin: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var mode by remember { mutableStateOf("login") } // "login" or "signup"
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=700&h=440&fit=crop&auto=format",
                contentDescription = "People enjoying a local activity together",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x2E0D0F14),
                                Color(0xB80D0F14)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1757F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "◈",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "JoinSphere",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                }
                Text(
                    text = "Find your people,\ndiscover your city.",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 31.sp,
                    letterSpacing = (-0.5).sp
                )
            }
        }

        // Form Section
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .fillMaxWidth()
        ) {
            // Tab Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF3F4F6))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val modes = listOf("login" to "Sign in", "signup" to "Create account")
                modes.forEach { (m, label) ->
                    val isSelected = mode == m
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { mode = m }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color(0xFF0D0F14) else Color(0xFF737880)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (mode == "signup") {
                    Field(label = "Full name") {
                        CustomTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = "Alex Johnson"
                        )
                    }
                }

                Field(label = "Email") {
                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "you@example.com",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }

                Field(label = "Password") {
                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "••••••••",
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { showPass = !showPass }) {
                                Icon(
                                    imageVector = if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (showPass) "Hide password" else "Show password",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color(0xFF737880)
                                )
                            }
                        }
                    )
                }
            }

            if (mode == "login") {
                Text(
                    text = "Forgot password?",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                        .clickable { /* Handle forgot password */ },
                    color = Color(0xFF1757F0),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button
            Button(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1757F0),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = if (mode == "login") "Continue" else "Create account",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
                Text(
                    text = "or",
                    color = Color(0xFF737880),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SocialButton(
                    label = "Google",
                    icon = "G",
                    iconColor = Color(0xFF4285F4),
                    modifier = Modifier.weight(1f),
                    onClick = onLogin
                )
                SocialButton(
                    label = "Apple",
                    icon = "⌘",
                    iconColor = Color.Black,
                    modifier = Modifier.weight(1f),
                    onClick = onLogin
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Text(
                text = "By continuing you agree to our Terms of Service and Privacy Policy",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color(0xFF737880)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

@Composable
fun Field(label: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0D0F14),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = placeholder, color = Color(0xFF9CA3AF)) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF3F4F6),
            focusedContainerColor = Color(0xFFF3F4F6),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        singleLine = true
    )
}

@Composable
fun SocialButton(
    label: String,
    icon: String,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF3F4F6)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = icon,
                color = iconColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0D0F14)
            )
        }
    }
}
