package com.fatmamuge.halyonetim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(stats: DashboardStats) {

    val currencyFormat = NumberFormat.getNumberInstance(
        Locale("tr", "TR")
    )

    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val paymentAmount =
        "${currencyFormat.format(stats.totalPaymentAmount)} TL"

    val salesAmount =
        "${currencyFormat.format(stats.totalSalesAmount)} TL"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Genel Bakış",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Hal Yönetim Sistemi",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(
                listOf(
                    DashboardItem(
                        title = "Kullanıcılar",
                        value = stats.totalUsers.toString(),
                        icon = Icons.Default.People
                    ),

                    DashboardItem(
                        title = "Ürünler",
                        value = stats.totalProducts.toString(),
                        icon = Icons.Default.Inventory2
                    ),

                    DashboardItem(
                        title = "Satışlar",
                        value = stats.totalSales.toString(),
                        icon = Icons.Default.ShoppingCart
                    ),

                    DashboardItem(
                        title = "Ödemeler",
                        value = stats.totalPayments.toString(),
                        icon = Icons.Default.Payment
                    ),

                    DashboardItem(
                        title = "Teslimatlar",
                        value = stats.totalProductDeliveries.toString(),
                        icon = Icons.Default.LocalShipping
                    ),

                    DashboardItem(
                        title = "Toplam Satış",
                        value = salesAmount,
                        icon = Icons.Default.AttachMoney
                    ),

                    DashboardItem(
                        title = "Toplam Ödeme",
                        value = paymentAmount,
                        icon = Icons.Default.AttachMoney
                    )
                )
            ) { item ->

                DashboardCard(
                    item = item
                )
            }
        }
    }
}


data class DashboardItem(
    val title: String,
    val value: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)


@Composable
fun DashboardCard(
    item: DashboardItem
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(28.dp)
                )

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = item.value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}