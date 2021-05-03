package chess.service;

import chess.dao.UserDao;
import chess.domain.User;
import chess.dto.response.UserResponse;
import chess.service.room.ChessRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessUserService {


    private final UserDao userDao;
    private final ChessRoomService chessRoomService;

    @Autowired
    public ChessUserService(final UserDao userDao, final ChessRoomService chessRoomService) {
        this.userDao = userDao;
        this.chessRoomService = chessRoomService;
    }

    public void create(final String name, final String pw) {
        userDao.create(name, pw);
    }

    public UserResponse user(final String name) {
        User user = userDao.findByName(name);
        return new UserResponse(user.getRoomId(), user.getName());
    }

    public UserResponse login(final String name, final String pw) {
        User user = userDao.findByName(name);

        if (!user.checkPassword(pw)) {
            throw  new IllegalArgumentException("비밀번호가 틀립니다.");
        }

        exitIfInGameWhenLogIn(user);
        return new UserResponse(user.getRoomId(), user.getName());
    }

    private void exitIfInGameWhenLogIn(User user) {
        if (user.inGame()) {
           exit(user.getRoomId(), user.getName());
        }
    }

    public void exit(final Long roomId, final String name) {
        chessRoomService.exit(roomId, name);
    }
}