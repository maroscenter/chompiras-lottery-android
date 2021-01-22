package com.youtube.sorcjc.billetero.model

// Body request to create a new ticket via the API
data class TicketBody (
    val lotteries: IntArray,
    val plays: ArrayList<TicketPlay>
)