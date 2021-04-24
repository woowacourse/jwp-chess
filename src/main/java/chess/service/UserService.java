package chess.service;

import chess.dao.UserDAO;
import chess.dto.user.UserDTO;
import chess.dto.user.UsersDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public UsersDTO participatedUsers(String id) {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setBlackUser(blackUserParticipatedInGame(id));
        usersDTO.setWhiteUser(whiteUserParticipatedInGame(id));
        return usersDTO;
    }

    public String blackUserParticipatedInGame(final String roomId) {
        return userDAO.findBlackUserByRoomId(roomId);
    }

    public String whiteUserParticipatedInGame(final String roomId) {
        return userDAO.findWhiteUserByRoomId(roomId);
    }

    public int registerUser(final String playerId, final String password) {
        Optional<UserDTO> user = userDAO.findByPlayerIdAndPassword(playerId, password);
        return user.orElse(userDAO.createUser(playerId, password)).getId();
    }
}
