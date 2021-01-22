package com.youtube.sorcjc.billetero.io.response

import com.youtube.sorcjc.billetero.model.Ticket
import com.youtube.sorcjc.billetero.model.TicketPlay

data class TicketResponse (
    val ticket: Ticket,
    val plays: ArrayList<TicketPlay>
)