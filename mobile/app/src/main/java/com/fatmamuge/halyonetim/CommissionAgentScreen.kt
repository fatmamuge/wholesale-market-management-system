package com.fatmamuge.halyonetim

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

data class CommissionSaleItem(
    val id: Int,
    val traderName: String,
    val productName: String,
    val quantity: Double,
    val totalPrice: Double,
    val date: String
)

data class CommissionStockItem(
    val id: Int,
    val productName: String,
    val producerName: String,
    val quantity: Double,
    val unitPrice: Double
)

data class CommissionAgentData(
    val agentName: String,
    val companyName: String,
    val totalSalesToday: Double,
    val activeStocks: List<CommissionStockItem>,
    val salesHistory: List<CommissionSaleItem>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommissionAgentScreen(
    data: CommissionAgentData,
    onLogoutClick: () -> Unit = {},
    onAddProductDeliveryClick: () -> Unit = {},
    onUploadDocumentClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Stoklar, 1: Satış Geçmişi
    val currencyFormat = NumberFormat.getNumberInstance(Locale("tr", "TR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Komisyoncu Paneli", fontWeight = FontWeight.Bold)
                        Text(
                            text = "${data.agentName} (${data.companyName})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Çıkış Yap")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onUploadDocumentClick,
                icon = { Icon(Icons.Default.DocumentScanner, contentDescription = null) },
                text = { Text("Belge Yükle (OCR)") }
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

            // Aksiyon Butonları (Ürün Kabul & Satış)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAddProductDeliveryClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.AddBox, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ürün Kaydet")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sekme Butonları
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
                    Text("Mevcut Stoklar (${data.activeStocks.size})")
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
                    Text("Satış Geçmişi")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (selectedTab == 0) {
                    items(data.activeStocks) { stock ->
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
                                    Text(text = stock.productName, fontWeight = FontWeight.Bold)
                                    Text(text = "Üretici: ${stock.producerName}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Birim Fiyat: ${currencyFormat.format(stock.unitPrice)} TL/kg", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                }
                                Text(
                                    text = "${stock.quantity.toInt()} Kg",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                } else {
                    items(data.salesHistory) { sale ->
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
                                    Text(text = "${sale.productName} (${sale.quantity.toInt()} Kg)", fontWeight = FontWeight.Bold)
                                    Text(text = "Alıcı: ${sale.traderName}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = sale.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                }
                                Text(
                                    text = "${currencyFormat.format(sale.totalPrice)} TL",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}