package com.youtube.sorcjc.billetero.model

import com.google.gson.annotations.SerializedName

/*
    {
        "id": 2,
        "reward": 10,
        "ticket_play_id": 5,
        "lottery_id": 1,
        "raffle_id": 1,
        "user_id": 2,
        "created_at": "2021-01-23T08:41:43.000000Z",
        "paid": 0,
        "ticket_play": {
            "id": 5,
            "number": "23",
            "points": 1,
            "type": "Quiniela",
            "ticket_id": 5
        },
        "lottery": {
            "id": 1,
            "name": "Lotería A",
            "abbreviated": "LA",
            "code": "123",
            "status": 1,
            "user_id": 1
        },
        "raffle": {
            "id": 1,
            "number_1": "25",
            "number_2": "23",
            "number_3": "12",
            "datetime": "2021-01-23 09:41:00",
            "lottery_id": 1
        }
    },
*/
data class Winner (
    val id: Int,
    val reward: Float,
    @SerializedName("created_at")
    val createdAt: String,
    val paid: Boolean,
    @SerializedName("ticket_play")
    val ticketPlay: TicketPlay,
    val lottery: Lottery
    // val raffle: Raffle
)