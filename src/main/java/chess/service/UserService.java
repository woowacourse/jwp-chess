package chess.service;

import chess.dao.UserDAO;
import chess.dto.UsersDTO;
import org.springframework.stereotype.Service;

@Service
public final class UserService {

    private final UserDAO userDAO;

    public UserService(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UsersDTO usersParticipatedInGame(final String roomId) {
        return userDAO.findUsersByRoomId(roomId);
    }

    public int userIdByNickname(final String nickname) {
        return userDAO.findUserIdByNickname(nickname);
    }

    public void enrollUser(final String nickname, final String password) {
        userDAO.insertUser(nickname, password);
    }
}
