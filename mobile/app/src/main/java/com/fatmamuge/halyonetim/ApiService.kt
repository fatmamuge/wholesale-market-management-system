package com.fatmamuge.halyonetim
import retrofit2.http.GET
import retrofit2.http.Header
interface ApiService {

    @GET("dashboard/stats")
    suspend fun getStats(@Header("Authorization") token: String): DashboardStats

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String): List<User>

    @GET("producer/deliveries")
    suspend fun getProducerDeliveries(@Header("Authorization") token: String): List<DeliveryItem>

    @GET("producer/payments")
    suspend fun getProducerPayments(@Header("Authorization") token: String): List<PaymentHistoryItem>

    @GET("commission-agent/stocks")
    suspend fun getCommissionStocks(@Header("Authorization") token: String): List<CommissionStockItem>

    @GET("commission-agent/sales")
    suspend fun getCommissionSales(@Header("Authorization") token: String): List<CommissionSaleItem>

    @GET("trader/market-products")
    suspend fun getMarketProducts(@Header("Authorization") token: String): List<MarketProductItem>

    @GET("trader/purchases")
    suspend fun getTraderPurchases(@Header("Authorization") token: String): List<PurchaseHistoryItem>
}