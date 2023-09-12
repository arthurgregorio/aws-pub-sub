package br.eti.arthurgregorio.snspub.domain.model;

import br.eti.arthurgregorio.snspub.application.payloads.ItemForm;

import java.math.BigDecimal;

public record Item(
        String description,
        Integer quantity,
        BigDecimal value
) {

    public static Item from(ItemForm form) {
        return new Item(form.description(), form.quantity(), form.value());
    }
}
