package chess.service;

import chess.domain.repository.user.UserRepository;
import chess.domain.user.User;
import chess.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long save(final User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(final Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("유저를 조회하는데 실패했습니다."));
    }

    @Transactional(readOnly = true)
    public User findByName(final String name) {
        Optional<User> optionalUser = userRepository.findByName(name);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("유저를 조회하는데 실패했습니다."));
    }

    public User findByRoomIdAndName(final Long roomId, String userName) {
        Optional<User> optionalUser = userRepository.findByRoomIdAndName(roomId, userName);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("유저를 조회하는데 실패했습니다."));
    }

    public void checkAccessibleUser(Long roomId, User user) {
        User findUser = findByRoomIdAndName(roomId, user.getName());
        findUser.checkPassword(user.getPassword());
    }
}
