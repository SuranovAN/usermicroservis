package suranovan.usermicroservice.service;

import org.springframework.stereotype.Service;
import suranovan.usermicroservice.entity.User;

import java.util.List;

@Service
public interface UserService {

    void saveUsers(List<User> userList);

    User saveUser(User user);

    User findById(long id);

    List<User> findAll();
}
