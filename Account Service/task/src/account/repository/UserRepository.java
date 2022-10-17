package account.repository;

import account.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
    @Query(value = "SELECT * FROM USER r ORDER BY r.user_id ASC", nativeQuery = true)
    List<User> findAllOrderById();

}
