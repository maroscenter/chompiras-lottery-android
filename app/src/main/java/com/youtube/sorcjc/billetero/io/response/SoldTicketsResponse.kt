package com.youtube.sorcjc.billetero.io.response

import com.youtube.sorcjc.billetero.model.Ticket

data class SoldTicketsResponse (
    val tickets: ArrayList<Ticket>
)