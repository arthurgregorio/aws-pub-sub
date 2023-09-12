package br.eti.arthurgregorio.snspub.domain.model;

import br.eti.arthurgregorio.snspub.application.payloads.ItemForm;
import br.eti.arthurgregorio.snspub.application.payloads.OrderForm;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Document(Order.COLLECTION_NAME)
public record Order(
        @Id @Field(targetType = FieldType.STRING) UUID id,
        @Field(targetType = FieldType.STRING) UUID number,
        String requester,
        BigDecimal total,
        List<Item> items
) {
    public static final String COLLECTION_NAME = "orders";

    public static Order from(OrderForm form) {

        final var total = form.items()
                .stream()
                .map(ItemForm::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final var orderItems = form.items()
                .stream()
                .map(Item::from)
                .toList();

        return new Order(UUID.randomUUID(), form.number(), form.requester(), total, orderItems);
    }
}
