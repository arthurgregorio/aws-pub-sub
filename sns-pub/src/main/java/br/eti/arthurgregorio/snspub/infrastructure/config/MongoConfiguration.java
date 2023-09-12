package br.eti.arthurgregorio.snspub.infrastructure.config;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

@EnableMongock
@Configuration(proxyBeanMethods = false)
public class MongoConfiguration {

    @Bean
    MongoTransactionManager transactionManager(MongoTemplate mongoTemplate) {

        final TransactionOptions transactionalOptions = TransactionOptions.builder()
                .readConcern(ReadConcern.MAJORITY)
                .readPreference(ReadPreference.primary())
                .writeConcern(WriteConcern.MAJORITY.withJournal(true))
                .build();

        return new MongoTransactionManager(mongoTemplate.getMongoDatabaseFactory(), transactionalOptions);
    }
}
