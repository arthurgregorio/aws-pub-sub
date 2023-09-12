package br.eti.arthurgregorio.snspub.domain.services;

import br.eti.arthurgregorio.snspub.infrastructure.repositories.OrderRepository;
import br.eti.arthurgregorio.snspub.domain.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateOrderService {

    private final ApplicationEventPublisher eventPublisher;

    private final OrderRepository orderRepository;

    public CreateOrderService(ApplicationEventPublisher eventPublisher, OrderRepository orderRepository) {
        this.eventPublisher = eventPublisher;
        this.orderRepository = orderRepository;
    }

    public void save(Order order) {

        log.debug("Trying to register order [{}]", order.number());

        orderRepository.save(order);
        log.info("Saved order [{}]", order.number());

        eventPublisher.publishEvent(new OrderCreatedEvent(order));
        log.info("Internal event published for order [{}]", order.number());
    }

    public record OrderCreatedEvent(Order order) {
    }
}
