package chess.service;

import chess.domain.user.User;
import chess.domain.user.UserRepository;
import chess.web.dto.user.UserRequestDto;
import chess.web.dto.user.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public long add(final UserRequestDto userRequestDto) {
        return userRepository.add(userRequestDto.toEntity());
    }

    public UserResponseDto findUserByName(final String name) {
        final User user = userRepository.findByName(name);
        return UserResponseDto.from(user);
    }

    public UserResponseDto findUserById(final long id) {
        final User user = userRepository.findById(id);
        return UserResponseDto.from(user);
    }

}
