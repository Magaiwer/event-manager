package dev.magaiver.user.domain.service;

import dev.magaiver.core.exception.BusinessException;
import dev.magaiver.user.domain.model.User;
import dev.magaiver.user.domain.repository.UserRepository;
import dev.magaiver.user.domain.service.exception.EmailAlreadyRegisteredException;
import dev.magaiver.user.domain.service.exception.RequiredPasswordException;
import dev.magaiver.user.domain.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User save(User user) {
        final Optional<User> userExisting = userRepository.findByEmail(user.getEmail());
        validateEmailExists(userExisting.orElse(null), user);
        requiredPassword(user);
        encodePassword(user);

        if (!StringUtils.hasText(user.getPassword())) {
            user.setPassword(userExisting.get().getPassword());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String password, String currentPassword) {
        final User user = findByIdOrElseThrow(userId);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException("Senha atual não confere com a senha do usuário");
        }
        user.setPassword(passwordEncoder.encode(password));
    }

    @Transactional(readOnly = true)
    public User findByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    private void validateEmailExists(User userExisting, User user) {
        if (userExisting != null && user.isNew()) {
            throw new EmailAlreadyRegisteredException(user.getEmail());
        }
    }

    private void requiredPassword(User user) {
        if (!StringUtils.hasText(user.getPassword()) && user.isNew()) {
            throw new RequiredPasswordException();
        }
    }

    private void encodePassword(User user) {
        if (user.isNew() || StringUtils.hasText(user.getPassword())) {
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        }
    }


}
