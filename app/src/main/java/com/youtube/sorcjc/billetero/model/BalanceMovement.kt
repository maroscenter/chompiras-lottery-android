package com.youtube.sorcjc.billetero.model

import com.google.gson.annotations.SerializedName

data class BalanceMovement (
        val description: String,
        val amount: Float,
        @SerializedName("created_at") val createdAt: String
)