package chess.service.spring;

import chess.domain.piece.TeamType;
import chess.domain.user.User;
import chess.domain.user.Users;
import chess.repository.spring.UserDAO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(String password, int roomId) {
        Users users = new Users(userDAO.findByRoomId(roomId));
        TeamType teamType = users.generateTeamType();
        String encodedPassword = passwordEncoder.encode(password);
        userDAO.insertUser(encodedPassword, teamType.toString(), roomId);
    }

    public void validateCurrentUser(String password, int roomId, TeamType teamType) {
        List<User> users = userDAO.findByRoomId(roomId);
        boolean isValidUser = users.stream()
                .anyMatch(user -> user.getTeamType().equals(teamType.toString()) && passwordEncoder.matches(password, user.getPassword()));
        if (!isValidUser) {
            throw new IllegalStateException("현재 차례가 아닙니다.");
        }
    }
}
