package com.fatmamuge.halyonetim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit
) {

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Hal Yönetim Sistemi"
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            label = {
                Text("Telefon Numarası")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Şifre")
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {

                scope.launch {

                    try {

                        isLoading = true
                        errorMessage = null



                        val request = LoginRequest(
                            phoneNumber = phoneNumber,
                            password = password
                        )

                        val response = withContext(Dispatchers.IO) {
                            RetrofitClient.loginApi.login(request)
                        }

                        onLoginSuccess(response.token)

                    } catch (e: Exception) {

                        errorMessage = e.message ?: "Giriş başarısız"

                    } finally {

                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {

            Text(
                text = if (isLoading) {
                    "Giriş yapılıyor..."
                } else {
                    "Giriş Yap"
                }
            )
        }

        if (errorMessage != null) {

            Text(
                text = "Hata: ${errorMessage}"
            )
        }
    }
}