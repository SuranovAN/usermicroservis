package suranovan.usermicroservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import suranovan.usermicroservice.entity.User;

import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumeService {
    private final UserService userService;
    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "newUserTopic", groupId = "newUser")
    public void consume(User user) {
        log.info("received user: {}", user);
        userService.saveUser(user);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
