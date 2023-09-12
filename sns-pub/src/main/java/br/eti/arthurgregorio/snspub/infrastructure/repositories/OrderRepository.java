package br.eti.arthurgregorio.snspub.infrastructure.repositories;

import br.eti.arthurgregorio.snspub.domain.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
