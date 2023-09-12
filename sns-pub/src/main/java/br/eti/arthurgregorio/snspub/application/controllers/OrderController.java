package br.eti.arthurgregorio.snspub.application.controllers;

import br.eti.arthurgregorio.snspub.application.payloads.OrderForm;
import br.eti.arthurgregorio.snspub.domain.OrderNotFoundException;
import br.eti.arthurgregorio.snspub.domain.model.Order;
import br.eti.arthurgregorio.snspub.domain.services.CreateOrderService;
import br.eti.arthurgregorio.snspub.infrastructure.repositories.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CreateOrderService createOrderService;

    public OrderController(OrderRepository orderRepository, CreateOrderService createOrderService) {
        this.orderRepository = orderRepository;
        this.createOrderService = createOrderService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody OrderForm form) {

        final var order = Order.from(form);

        createOrderService.save(order);

        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable UUID id) {

        final var order = orderRepository.findById(id.toString())
                .orElseThrow(() -> new OrderNotFoundException(id));

        return ResponseEntity.ok(order);
    }
}
