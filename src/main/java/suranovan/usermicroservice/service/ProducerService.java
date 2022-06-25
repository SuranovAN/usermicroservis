package suranovan.usermicroservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import suranovan.usermicroservice.entity.User;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaTemplate<String, User> kafkaTemplate;

    @SneakyThrows
    public void produce(String topic, User user) {
        log.info( "produce new user: {}", user);
        kafkaTemplate.send(topic, user);
    }
}
