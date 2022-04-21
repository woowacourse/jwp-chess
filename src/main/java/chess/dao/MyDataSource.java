package chess.dao;

import java.sql.Connection;

public interface MyDataSource {

    Connection getConnection();

    void close();
}
