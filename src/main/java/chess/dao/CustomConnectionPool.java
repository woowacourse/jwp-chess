package chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomConnectionPool implements ConnectionPool {
    private static final int INITIAL_POOL_SIZE = 15;

    private final ConnectionProperty connectionProperty;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();

    private CustomConnectionPool(final ConnectionProperty connectionProperty, final List<Connection> connectionPool) {
        this.connectionProperty = connectionProperty;
        this.connectionPool = connectionPool;
    }

    public static CustomConnectionPool create(final ConnectionProperty connectionProperty) {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(connectionProperty));
        }
        return new CustomConnectionPool(connectionProperty, pool);
    }

    private static Connection createConnection(final ConnectionProperty connectionProperty) {
        // 드라이버 연결
        try {
            return DriverManager.getConnection(connectionProperty.getUrl(), connectionProperty.getUrl(), connectionProperty.getPassword());
        } catch (SQLException e) {
            System.err.println("연결 오류:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Connection getConnection() {
        if (connectionPool.isEmpty()) {
            validateSize();
        }
        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);
        try {
            if (!connection.isValid(1_000)) {
                connection = createConnection(connectionProperty);
            }

        } catch (SQLException e) {
            System.err.println("시간 오류" + e.getMessage());
            e.printStackTrace();
        }

        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(final Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    public void shutdown() {
        usedConnections.forEach(this::releaseConnection);
        connectionPool.forEach(c -> {
            try {
                c.close();
            } catch (SQLException e) {
                System.err.println("커넥션 해제 오류" + e.getMessage());
                e.printStackTrace();
            }
        });
        connectionPool.clear();
    }

    private void validateSize() {
        if (usedConnections.size() < INITIAL_POOL_SIZE) {
            connectionPool.add(createConnection(connectionProperty));
            return;
        }
        throw new RuntimeException("Pool 사이즈를 넘어섰습니다.");
    }
}
