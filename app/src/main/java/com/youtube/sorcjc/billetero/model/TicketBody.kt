package com.youtube.sorcjc.billetero.model

data class TicketBody (
    val lotteries: IntArray,
    val plays: ArrayList<TicketPlay>
)