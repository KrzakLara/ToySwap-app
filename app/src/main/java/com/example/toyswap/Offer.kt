package com.example.toyswap

import retrofit2.http.Tag
import java.math.BigDecimal
import java.util.concurrent.locks.Condition

data class Offer(
    val id: Long,
    val title: String,
    val description: String,
    val condition: Condition,
    val price: BigDecimal,
    val tags: List<Tag>
)

