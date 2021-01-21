package com.youtube.sorcjc.billetero.model

data class TicketBody (
    val lottery_ids: IntArray,
    val plays: ArrayList<TicketPlay>
)