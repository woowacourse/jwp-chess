package chess.service;

import chess.dao.UserDao;
import chess.entity.User;
import chess.exception.*;
import chess.service.dto.UserFindResponseDto;
import chess.service.dto.UserSaveRequestDto;
import chess.service.dto.UserSignRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public void save(final UserSaveRequestDto requestDto) {
        if (userDao.findByName(requestDto.getName()).isPresent()) {
            throw new DuplicateUserException();
        }
        if ("".equals(requestDto.getName())) {
            throw new UserNameNullException();
        }
        if ("".equals(requestDto.getPassword())) {
            throw new UserPasswordNullException();
        }
        User user = requestDto.toEntity();
        userDao.save(user);
    }

    @Transactional(readOnly = true)
    public UserFindResponseDto findByName(final String name) {
        Optional<User> user = userDao.findByName(name);
        if (user.isPresent()) {
            return new UserFindResponseDto(user.get().getName());
        }
        throw new NotExistUserException();
    }

    @Transactional(readOnly = true)
    public void signIn(final UserSignRequestDto requestDto) {
        User user = userDao.findByName(requestDto.getName())
                .orElseThrow(NotExistUserException::new);
        if (!user.samePassword(requestDto.getPassword())) {
            throw new InvalidPasswordException();
        }
    }
}
