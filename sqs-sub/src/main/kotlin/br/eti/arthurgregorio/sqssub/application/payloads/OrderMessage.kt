package br.eti.arthurgregorio.sqssub.application.payloads

import java.math.BigDecimal
import java.util.*

data class OrderMessage(
    val number: UUID,
    val requester: String,
    val total: BigDecimal,
    val items: List<OrderItemMessage>
)

data class OrderItemMessage(
    val description: String,
    val quantity: Int,
    val value: BigDecimal
)
