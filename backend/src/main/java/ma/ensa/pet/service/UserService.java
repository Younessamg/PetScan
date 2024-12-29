package ma.ensa.pet.service;

import ma.ensa.pet.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    boolean existsByEmail(String email);
}