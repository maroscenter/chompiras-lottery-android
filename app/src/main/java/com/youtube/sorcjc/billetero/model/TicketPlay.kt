package com.youtube.sorcjc.billetero.model

import com.google.gson.annotations.SerializedName

data class TicketPlay (
        val number: String,
        val points: Int,
        val type: String,

        @SerializedName("ticket_id")
        val ticketId: Int
)