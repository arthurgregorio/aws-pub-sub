package br.eti.arthurgregorio.sqssub.domain.services

import br.eti.arthurgregorio.sqssub.domain.model.Order
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class OrderProcessingService {

    fun process(order: Order) {
        logger.info { "Processing order [${order.number}]" }
    }
}
