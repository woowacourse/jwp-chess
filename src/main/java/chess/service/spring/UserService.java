package chess.service.spring;

import chess.domain.piece.TeamType;
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

    public void addUser(int roomId, String password) {
        Users users = new Users(userDAO.findByRoomId(roomId));
        TeamType teamType = users.generateTeamType();
        String encodedPassword = passwordEncoder.encode(password);
        userDAO.insertUser(encodedPassword, teamType.toString(), roomId);
    }

    public void validateCurrentUser(int roomId, String password, TeamType teamType) {
        Users users = new Users(userDAO.findByRoomId(roomId));
        String encodedPassword = passwordEncoder.encode(password);
        boolean isValidTurn = users.hasEqualUser(encodedPassword, teamType);
        if (!isValidTurn) {
            throw new IllegalStateException("현재 차례가 아닙니다.");
        }
    }
}
