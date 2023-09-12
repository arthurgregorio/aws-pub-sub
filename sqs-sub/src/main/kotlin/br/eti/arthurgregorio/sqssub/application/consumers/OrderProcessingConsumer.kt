package br.eti.arthurgregorio.sqssub.application.consumers

import br.eti.arthurgregorio.sqssub.application.payloads.OrderMessage
import br.eti.arthurgregorio.sqssub.domain.model.Item
import br.eti.arthurgregorio.sqssub.domain.model.Order
import br.eti.arthurgregorio.sqssub.domain.services.OrderProcessingService
import br.eti.arthurgregorio.sqssub.infrastructure.utils.currentConsumeAttempt
import io.awspring.cloud.sqs.annotation.SqsListener
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class CreateCustomerCommandConsumer(
    private val orderProcessingService: OrderProcessingService
) {

    @SqsListener("\${application.sqs.queues.order-processing}")
    fun onMessage(message: OrderMessage, @Headers headers: MessageHeaders) {

        logger.info { "Message [${headers.id}] received to process order number [${message.number}]" }
        logger.debug { "Trying to consume message [${headers.id}] at [${headers.currentConsumeAttempt()}] attempt" }

        val items = message.items.map { Item(it.description, it.quantity, it.value) }
        val order = Order(message.number, message.requester, message.total, items)

        orderProcessingService.process(order)
    }
}
