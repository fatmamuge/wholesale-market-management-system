package com.fatmamuge.halyonetim

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

// ER Diyagramı ve Gereksinim Analizine Uygun Veri Modelleri
data class DeliveryItem(
    val id: Int,
    val productName: String,
    val quantity: Double,
    val unit: String,
    val commissionAgentName: String,
    val deliveryDate: String,
    val status: String // "Tamamlandı", "Komisyonda", "Beklemede"
)

data class PaymentHistoryItem(
    val id: Int,
    val commissionAgentName: String,
    val amount: Double,
    val paymentDate: String,
    val status: String // "Ödendi", "Bekliyor"
)

data class ProducerDashboardData(
    val producerName: String,
    val totalDeliveredKg: Double,
    val totalEarnings: Double,
    val pendingEarnings: Double,
    val deliveries: List<DeliveryItem>,
    val payments: List<PaymentHistoryItem>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProducerScreen(
    data: ProducerDashboardData,
    onLogoutClick: () -> Unit = {}
) {
    // 0: Teslim Edilen Ürünler, 1: Ödeme Geçmişi
    var selectedTab by remember { mutableStateOf(0) }

    val currencyFormat = NumberFormat.getNumberInstance(Locale("tr", "TR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Üretici Portalı",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = data.producerName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Çıkış Yap"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. ÖZET BİLGİ KARTLARI (Dashboard Cards)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    title = "Teslim Edilen",
                    value = "${data.totalDeliveredKg.toInt()} Kg",
                    icon = Icons.Default.Inventory2,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Tahsil Edilen",
                    value = "${currencyFormat.format(data.totalEarnings)} TL",
                    icon = Icons.Default.Paid,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. MODÜL SEÇİM BUTONLARI (Tab Bar)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selectedTab == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Teslimatlar", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selectedTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ödemeler", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. LİSTE ALANI (LazyColumn)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (selectedTab == 0) {
                    // Teslim Ettiği Ürünler & Hareketleri
                    items(data.deliveries) { item ->
                        DeliveryCardItem(item = item)
                    }
                } else {
                    // Ödeme Geçmişi
                    items(data.payments) { payment ->
                        PaymentCardItem(payment = payment, format = currencyFormat)
                    }
                }
            }
        }
    }
}

// Üst Özet Kartı
@Composable
fun SummaryCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
            }
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

// Teslim Edilen Ürün Kartı
@Composable
fun DeliveryCardItem(item: DeliveryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = item.productName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Komisyoncu: ${item.commissionAgentName}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Tarih: ${item.deliveryDate}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${item.quantity} ${item.unit}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (item.status == "Tamamlandı") Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                ) {
                    Text(
                        text = item.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (item.status == "Tamamlandı") Color(0xFF2E7D32) else Color(0xFFE65100),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Ödeme Kartı
@Composable
fun PaymentCardItem(payment: PaymentHistoryItem, format: NumberFormat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = payment.commissionAgentName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Tarih: ${payment.paymentDate}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${format.format(payment.amount)} TL",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (payment.status == "Ödendi") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = payment.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (payment.status == "Ödendi") Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProducerScreenPreview() {
    MaterialTheme {
        ProducerScreen(
            data = ProducerDashboardData(
                producerName = "Fatma Müge (Üretici)",
                totalDeliveredKg = 12500.0,
                totalEarnings = 85400.0,
                pendingEarnings = 14200.0,
                deliveries = listOf(
                    DeliveryItem(1, "Domates (Salkım)", 3200.0, "Kg", "Özkan Komisyonculuk", "14.05.2026", "Tamamlandı"),
                    DeliveryItem(2, "Salatalık (Sera)", 1800.0, "Kg", "Akdeniz Tarım Komisyon", "16.05.2026", "Komisyonda"),
                    DeliveryItem(3, "Biber (Kapya)", 950.0, "Kg", "Yıldız Hal Ltd.", "18.05.2026", "Beklemede")
                ),
                payments = listOf(
                    PaymentHistoryItem(1, "Özkan Komisyonculuk", 45000.0, "15.05.2026", "Ödendi"),
                    PaymentHistoryItem(2, "Akdeniz Tarım Komisyon", 40400.0, "17.05.2026", "Ödendi"),
                    PaymentHistoryItem(3, "Yıldız Hal Ltd.", 14200.0, "19.05.2026", "Bekliyor")
                )
            )
        )
    }
}