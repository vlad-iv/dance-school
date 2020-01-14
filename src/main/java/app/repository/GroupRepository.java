package app.repository;

import app.model.Group;
import app.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Group repository.
 *
 * @author Vladimir Ivanov (ivanov.vladimir.l@gmail.com)
 */
@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
    @Override
    List<Group> findAll();
}
