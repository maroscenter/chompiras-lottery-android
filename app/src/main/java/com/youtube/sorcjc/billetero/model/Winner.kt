package com.youtube.sorcjc.billetero.model

import com.google.gson.annotations.SerializedName

data class Winner (
    val id: Int,
    val reward: Float,

    @SerializedName("created_at")
    val createdAt: String,

    val paid: Boolean,

    @SerializedName("ticket_play")
    val ticketPlay: TicketPlay,

    val lottery: Lottery,

    @SerializedName("ticket_code")
    val ticketCode: String

    // val raffle: Raffle
)