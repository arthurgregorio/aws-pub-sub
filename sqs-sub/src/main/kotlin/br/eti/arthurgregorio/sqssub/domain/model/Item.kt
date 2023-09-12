package br.eti.arthurgregorio.sqssub.domain.model

import java.math.BigDecimal

data class Item(val description: String, val quantity: Int, val value: BigDecimal)
