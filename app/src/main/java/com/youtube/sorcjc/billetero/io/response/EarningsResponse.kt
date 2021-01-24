package com.youtube.sorcjc.billetero.io.response

data class EarningsResponse (val total: Total) {
    data class Total (
            val income: Float,
            val commission: Float
    )
}