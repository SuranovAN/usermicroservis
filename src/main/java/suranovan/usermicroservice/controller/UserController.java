package suranovan.usermicroservice.controller;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suranovan.usermicroservice.entity.User;
import lombok.RequiredArgsConstructor;
import suranovan.usermicroservice.service.ProducerService;
import suranovan.usermicroservice.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProducerService producerService;

    @SneakyThrows
    @PutMapping("/v1/user/save")
    public void produceNewUserToKafka(@RequestBody User user) {
        producerService.produce("newUserTopic", user);
    }

    @SneakyThrows
    @GetMapping("/v1/users")
    public ResponseEntity<List<User>> allUser() {
        return ResponseEntity.ok(userService.findAll());
    }

    @SneakyThrows
    @GetMapping("/v1/user")
    public ResponseEntity<User> userById(@RequestParam(name = "id") long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}
