package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.domain.user.User;
import wooteco.chess.domain.user.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return userRepository.save(user);
    }
}
