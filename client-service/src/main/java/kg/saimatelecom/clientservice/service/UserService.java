package kg.saimatelecom.clientservice.service;

import kg.saimatelecom.clientservice.dto.response.UserPersonalAccountResponse;
import kg.saimatelecom.clientservice.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long userId);

    User saveUser(User user);

    boolean userExistsByEmail(String email);

    Optional<User> findUserByEmail(String userEmail);

    UserPersonalAccountResponse findByEmail(String email);

    Optional<User> findByActivationToken(String token);

    BigDecimal getPersonalBalance(String username);
}
