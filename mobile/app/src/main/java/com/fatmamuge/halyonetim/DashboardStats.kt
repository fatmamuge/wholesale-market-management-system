package com.fatmamuge.halyonetim

data class DashboardStats(
    val totalUsers: Long,
    val totalProducts: Long,
    val totalSales: Long,
    val totalPaymentAmount: Double,
    val totalPayments: Long,
    val totalProductDeliveries: Long,
    val totalSalesAmount: Double
)