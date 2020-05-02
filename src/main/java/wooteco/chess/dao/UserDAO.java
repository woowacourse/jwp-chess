package wooteco.chess.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.stereotype.Component;
import wooteco.chess.domain.player.User;
import wooteco.chess.util.DBConnector;

@Component
public class UserDAO {

    private DBConnector dbConnector;

    public UserDAO(final DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void addUser(User user) throws SQLException {
        dbConnector.executeUpdate("INSERT INTO user(name) VALUES (?)", user.getName());
    }

    public Optional<User> findByUserName(String userName) throws SQLException {
        ResultSet rs = dbConnector.executeQuery("SELECT * FROM user WHERE name = ?", userName);
        if (!rs.next()) {
            return Optional.empty();
        }

        return Optional.ofNullable(new User(rs.getString("name")));
    }

    public void deleteUserByUserName(String name) throws SQLException {
        dbConnector.executeUpdate("DELETE FROM user WHERE name = ?", name);
    }
}