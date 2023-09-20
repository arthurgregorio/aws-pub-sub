package br.eti.arthurgregorio.snspub.domain.producers;

import br.eti.arthurgregorio.snspub.domain.services.CreateOrderService;
import io.awspring.cloud.sns.core.SnsNotification;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreatedNotificationProducer {

    private final String topic;

    private final SnsTemplate snsTemplate;

    public OrderCreatedNotificationProducer(
            @Value("${sns.order.notification-topic}") String topic, SnsTemplate snsTemplate) {
        this.topic = topic;
        this.snsTemplate = snsTemplate;
    }

    @Async
    @EventListener
    public void listen(CreateOrderService.OrderCreatedEvent event) {

        final var order = event.order();

        log.debug("Trying to send notification about order [{}]", order.number());
        snsTemplate.sendNotification(topic, SnsNotification.of(order));
        log.info("Notification about order [{}] sent", order.number());
    }
}
