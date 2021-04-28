package chess.service.spring;

import chess.domain.piece.TeamType;
import chess.domain.user.User;
import chess.domain.user.Users;
import chess.repository.spring.UserDAO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUserIntoRoom(int roomId, String password) {
        Users usersInRoom = new Users(userDAO.findAllByRoomId(roomId));
        if (usersInRoom.hasMaximumCountsForGame()) {
            throw new IllegalStateException("이미 꽉 찬 방입니다.");
        }
        String encodedPassword = passwordEncoder.encode(password);
        TeamType teamType = usersInRoom.generateCurrentTeamType();
        userDAO.insertUser(encodedPassword, teamType.toString(), roomId);
    }

    public void validateUserTurn(int roomId, String password, TeamType teamType) {
        Users users = new Users(userDAO.findAllByRoomId(roomId));
        if (!users.hasMaximumCountsForGame()) {
            throw new IllegalStateException("혼자서는 플레이할 수 없습니다.");
        }
        boolean isValidTurn = users.filterByTeam(teamType)
                .stream()
                .anyMatch(user -> passwordEncoder.matches(password, user.getPassword()));
        if (!isValidTurn) {
            throw new IllegalStateException("현재 차례가 아닙니다.");
        }
    }

    public void deleteAllByRoomId(int roomId) {
        userDAO.deleteAllByRoomId(roomId);
    }

    public void deleteUser(int roomId, String password) {
        User targetUser = userDAO.findAllByRoomId(roomId)
                .stream()
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("삭제할 유저가 존재하지 않습니다."));
        userDAO.deleteUser(targetUser);
    }
}
