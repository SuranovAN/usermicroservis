package suranovan.usermicroservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import suranovan.usermicroservice.entity.User;
import suranovan.usermicroservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void saveUsers(List<User> userList) {
        userRepository.saveAllAndFlush(userList);
    }

    @Override
    @CachePut(key = "#user.id")
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Cacheable(key = "#id")
    public User findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
