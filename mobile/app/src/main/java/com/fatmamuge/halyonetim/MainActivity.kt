package com.fatmamuge.halyonetim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fatmamuge.halyonetim.ui.theme.HalYonetimTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HalYonetimTheme {
                var token by remember { mutableStateOf<String?>(null) }
                var role by remember { mutableStateOf<String?>(null) }

                // Veritabanı Durumları (Database States)
                var stats by remember { mutableStateOf<DashboardStats?>(null) }
                var users by remember { mutableStateOf<List<User>>(emptyList()) }
                var producerData by remember { mutableStateOf<ProducerDashboardData?>(null) }
                var commissionData by remember { mutableStateOf<CommissionAgentData?>(null) }
                var traderData by remember { mutableStateOf<TraderDashboardData?>(null) }

                var isLoading by remember { mutableStateOf(false) }
                var errorMessage by remember { mutableStateOf<String?>(null) }

                // Rol ve Token Belirlendiğinde İlgili Tablolardan Canlı Veri Çek
                LaunchedEffect(token, role) {
                    if (token != null && role != null) {
                        isLoading = true
                        errorMessage = null
                        val authHeader = "Bearer $token"

                        try {
                            withContext(Dispatchers.IO) {
                                when (role) {
                                    "General Manager" -> {
                                        stats = RetrofitClient.apiService.getStats(authHeader)
                                        users = RetrofitClient.apiService.getUsers(authHeader)
                                    }

                                    "Producer" -> {
                                        val deliveries = try {
                                            RetrofitClient.apiService.getProducerDeliveries(authHeader)
                                        } catch (e: Exception) { emptyList() }

                                        val payments = try {
                                            RetrofitClient.apiService.getProducerPayments(authHeader)
                                        } catch (e: Exception) { emptyList() }

                                        val totalKg = deliveries.sumOf { it.quantity }
                                        val totalPaid = payments.filter { it.status == "Ödendi" }.sumOf { it.amount }
                                        val pending = payments.filter { it.status != "Ödendi" }.sumOf { it.amount }

                                        producerData = ProducerDashboardData(
                                            producerName = "Kayıtlı Üretici",
                                            totalDeliveredKg = totalKg,
                                            totalEarnings = totalPaid,
                                            pendingEarnings = pending,
                                            deliveries = deliveries,
                                            payments = payments
                                        )
                                    }

                                    "Commission Agent" -> {
                                        val stocks = try {
                                            RetrofitClient.apiService.getCommissionStocks(authHeader)
                                        } catch (e: Exception) { emptyList() }

                                        val sales = try {
                                            RetrofitClient.apiService.getCommissionSales(authHeader)
                                        } catch (e: Exception) { emptyList() }

                                        val salesSum = sales.sumOf { it.totalPrice }

                                        commissionData = CommissionAgentData(
                                            agentName = "Komisyon Şubesi",
                                            companyName = "Hal No: 12",
                                            totalSalesToday = salesSum,
                                            activeStocks = stocks,
                                            salesHistory = sales
                                        )
                                    }

                                    "Trader" -> {
                                        val market = try {
                                            RetrofitClient.apiService.getMarketProducts(authHeader)
                                        } catch (e: Exception) { emptyList() }

                                        val history = try {
                                            RetrofitClient.apiService.getTraderPurchases(authHeader)
                                        } catch (e: Exception) { emptyList() }

                                        traderData = TraderDashboardData(
                                            traderName = "Tüccar",
                                            companyName = "Hal Alıcı Portalı",
                                            marketProducts = market,
                                            purchaseHistory = history
                                        )
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            errorMessage = "Veritabanı bağlantı hatası: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                }

                if (token == null) {
                    LoginScreen(
                        onLoginSuccess = { newToken ->
                            token = newToken
                            role = JwtUtils.getRole(newToken)
                        }
                    )
                } else if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (errorMessage != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = errorMessage!!)
                    }
                } else {
                    when (role) {
                        "General Manager" -> {
                            if (stats != null) {
                                GeneralManagerScreen(
                                    data = GeneralManagerData(
                                        managerName = "Hal Müdürü",
                                        totalUsers = stats!!.totalUsers.toInt(),
                                        totalProducts = stats!!.totalProducts.toInt(),
                                        totalDeliveries = stats!!.totalProductDeliveries.toInt(),
                                        totalSalesAmount = stats!!.totalSalesAmount,
                                        totalPaymentAmount = stats!!.totalPaymentAmount,
                                        aiInsightSummary = "Veritabanından ${stats!!.totalProductDeliveries} teslimat ve ${stats!!.totalProducts} ürün verisi analiz edildi.",
                                        userList = users.map { u ->
                                            UserSummaryItem(
                                                id = u.id?.toInt() ?: 0,
                                                nameSurname = u.nameSurname ?: "Kullanıcı",
                                                role = "Sistem Kullanıcısı",
                                                phoneNumber = u.phoneNumber ?: "-"
                                            )
                                        },
                                        recentMovements = emptyList()
                                    ),
                                    onLogoutClick = {
                                        token = null
                                        role = null
                                    }
                                )
                            }
                        }

                        "Producer" -> {
                            producerData?.let { data ->
                                ProducerScreen(
                                    data = data,
                                    onLogoutClick = { token = null; role = null }
                                )
                            }
                        }

                        "Commission Agent" -> {
                            commissionData?.let { data ->
                                CommissionAgentScreen(
                                    data = data,
                                    onLogoutClick = { token = null; role = null }
                                )
                            }
                        }

                        "Trader" -> {
                            traderData?.let { data ->
                                TraderScreen(
                                    data = data,
                                    onLogoutClick = { token = null; role = null }
                                )
                            }
                        }

                        else -> {
                            Text(text = "Bilinmeyen rol: $role")
                        }
                    }
                }
            }
        }
    }
}