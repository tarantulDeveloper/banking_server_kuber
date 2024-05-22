package kg.saimatelecom.clientservice.service.impl;

import kg.saimatelecom.clientservice.dto.response.UserPersonalAccountResponse;
import kg.saimatelecom.clientservice.exceptions.ResourceNotFoundException;
import kg.saimatelecom.clientservice.model.ClientAccount;
import kg.saimatelecom.clientservice.model.User;
import kg.saimatelecom.clientservice.repository.ClientAccountRepository;
import kg.saimatelecom.clientservice.repository.UserRepository;
import kg.saimatelecom.clientservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    ClientAccountRepository clientAccountRepository;
    JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public UserPersonalAccountResponse findByEmail(String email) {
        String sql = "SELECT u.firstName, u.lastName, u.patronymic, u.email, u.phoneNumber, u.profileImagePath, d.personalNumber, d.documentId, d.authority, d.documentIssuedAt, d.documentExpiresAt, d.citizenship, d.birthDate, c.balance " +
                "FROM User u, Document d, ClientAccount c " +
                "WHERE u.email = ? AND u.id = d.user_id AND c.user_id = u.id";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) ->
                new UserPersonalAccountResponse(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("patronymic"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("profileImagePath"),
                        rs.getString("personalNumber"),
                        rs.getString("documentId"),
                        rs.getString("authority"),
                        rs.getTimestamp("documentIssuedAt"),
                        rs.getTimestamp("documentExpiresAt"),
                        rs.getString("citizenship"),
                        rs.getTimestamp("birthDate"),
                        rs.getBigDecimal("balance")
                )
        );

    }

    @Override
    public Optional<User> findByActivationToken(String token) {
        return userRepository.findByActivationToken(token);
    }

    @Override
    public BigDecimal getPersonalBalance(String username) {
        ClientAccount clientAccount = clientAccountRepository.findByEmail(username).orElseThrow(
                () -> new ResourceNotFoundException("User with email " + username + " not found")
        );
        return clientAccount.getBalance();
    }
}