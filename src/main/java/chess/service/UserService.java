package chess.service;

import chess.dao.UserDao;
import chess.entity.User;
import chess.exception.DuplicateRoomException;
import chess.service.dto.UserSaveRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public void save(final UserSaveRequestDto requestDto) {
        if (userDao.findByName(requestDto.getName()).isPresent()) {
            throw new DuplicateRoomException();
        }
        User user = requestDto.toEntity();
        userDao.save(user);
    }
}
