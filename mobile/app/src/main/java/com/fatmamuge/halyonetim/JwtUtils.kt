package com.fatmamuge.halyonetim

import android.util.Base64
import org.json.JSONObject

object JwtUtils {

    fun getRole(token: String): String? {

        return try {

            val parts = token.split(".")

            if (parts.size != 3) {
                return null
            }

            val payload = String(
                Base64.decode(
                    parts[1],
                    Base64.URL_SAFE or Base64.NO_WRAP
                ),
                Charsets.UTF_8
            )

            val json = JSONObject(payload)

            json.optString("role", null)

        } catch (e: Exception) {

            null
        }
    }
}