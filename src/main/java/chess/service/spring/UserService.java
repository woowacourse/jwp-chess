package chess.service.spring;

import chess.domain.piece.TeamType;
import chess.domain.user.User;
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

    public void addUser(String password, int roomId, TeamType teamType) {
        List<User> users = userDAO.findByRoomId(roomId);
        if (users.size() == 2) {
            throw new IllegalStateException("이미 꽉 찬 방입니다.");
        }
        String encodedPassword = passwordEncoder.encode(password);
        userDAO.insertUser(encodedPassword, teamType.toString(), roomId);
    }
}
