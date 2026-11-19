package com.fatmamuge.halyonetim

data class User(
    val id: Int,
    val nameSurname: String,
    val phoneNumber: String,
    val passwordHash: String?,
    val createdAt: String?,
    val role: Role?
)

data class Role(
    val id: Int,
    val name: String?
)