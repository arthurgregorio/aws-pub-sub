package br.eti.arthurgregorio.snspub.application.payloads;

import java.math.BigDecimal;

public record ItemForm(
        String description,
        Integer quantity,
        BigDecimal value
) {

    public BigDecimal calculateItemTotal() {
        return value().multiply(new BigDecimal(quantity()));
    }
}
