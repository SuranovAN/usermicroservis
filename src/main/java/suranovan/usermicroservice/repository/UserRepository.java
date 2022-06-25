package suranovan.usermicroservice.repository;

import org.springframework.stereotype.Repository;
import suranovan.usermicroservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
}
