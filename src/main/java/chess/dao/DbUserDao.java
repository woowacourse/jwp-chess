package chess.dao;

import chess.domain.user.User;
import chess.util.DateTimeConvertUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbUserDao implements UserDao {

    @Override
    public void save(User user) {
        final Connection connection = MysqlDao.getConnection();
        final String sql = "insert users(user_name, created_at) values(?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setTimestamp(2, DateTimeConvertUtil.toTimestampFrom(user.getCreatedAt()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public User findByName(String name) {
        final Connection connection = MysqlDao.getConnection();
        final String sql = "select * from users where user_name = ?";
        final PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return new User(
                    resultSet.getString("user_name"),
                    resultSet.getTimestamp("created_at")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public List<User> findAll() {
        final Connection connection = MysqlDao.getConnection();
        final String sql = "select * from users";
        final PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            final List<User> findUsers = new ArrayList<>();
            while (resultSet.next()) {
                final User findUser = new User(
                        resultSet.getString("user_name"),
                        resultSet.getTimestamp("created_at")
                );
                findUsers.add(findUser);
            }
            return findUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteByName(String name) {
        final Connection connection = MysqlDao.getConnection();
        final String sql = "delete from users where user_name = ?";
        final PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteAll() {
        final Connection connection = MysqlDao.getConnection();
        final String sql = "delete from users";
        final PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
