package app.service;

import app.model.User;
import app.model.UserRole;
import app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserDetailsAfterAuthentication(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Set<UserRole> getRoles() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(auth.getName());
        if (user == null) {
            return Collections.emptySet();
        }
        return user.getRoles();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
