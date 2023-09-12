package br.eti.arthurgregorio.snspub.domain;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID id) {
        super("Can't find any order with ID [%s]".formatted(id));
    }
}
