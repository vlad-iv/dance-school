package app.repository;

import app.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User repository.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByLoginAndPassword(String login, String password);

    User findByLogin(String login);

    @Override
    List<User> findAll();
}
