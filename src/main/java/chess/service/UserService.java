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
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException("유저를 조회하는데 실패했습니다.");
    }

    //TODO 유저 입장 시 두 플레이어 모두 입장한 상태에서 2명 외의 다른 이름으로 접속을 요청하면 들어갈 수 없는 방입니다 같은 예외 던지기
    @Transactional(readOnly = true)
    public User findByName(final String name) {
        Optional<User> optionalUser = userRepository.findByName(name);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException("유저를 조회하는데 실패했습니다.");
    }
}
