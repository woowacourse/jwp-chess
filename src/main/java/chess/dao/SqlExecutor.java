package chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class SqlExecutor {
    private final Connection connection;

    private SqlExecutor(Connection connection) {
        this.connection = connection;
    }

    public static SqlExecutor getInstance() {
        return new SqlExecutor(MysqlConnector.getConnection());
    }

    public <T> T select(final StatementLoader statementLoader, final ResultSetSerializer<T> serializer) {
        try (final PreparedStatement statement = statementLoader.create(connection)) {
            return serializer.serialize(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("select SQL 실행 에러!");
        }
    }

    public void insert(final StatementLoader statementLoader) {
        try (final PreparedStatement statement = statementLoader.create(connection)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("insert SQL 실행 에러!");
        }
    }

    public Long insertAndGetGeneratedKey(final StatementLoader statementLoader) {
        try (final PreparedStatement statement = statementLoader.create(connection)) {
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("insert SQL 실행 에러!");
        }
        throw new NoSuchElementException("DB에서 데이터를 찾지 못했습니다.");
    }

    public void update(final StatementLoader statementLoader) {
        try (final PreparedStatement statement = statementLoader.create(connection)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("update SQL 실행 에러!");
        }
    }

    public void delete(final StatementLoader statementLoader) {
        try (final PreparedStatement statement = statementLoader.create(connection)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("delete SQL 실행 에러!");
        }
    }
}
