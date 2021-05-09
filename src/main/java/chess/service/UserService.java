package chess.service;

import chess.domain.repository.user.UserRepository;
import chess.domain.user.User;
import chess.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long save(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException("유저를 조회하는데 실패했습니다.");
    }

    public User findByName(String name) {
        Optional<User> optionalUser = userRepository.findByName(name);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException("유저를 조회하는데 실패했습니다.");
    }
}
