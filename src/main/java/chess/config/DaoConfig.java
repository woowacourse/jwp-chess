package chess.config;

import chess.database.vanillajdbc.dao.BoardDao;
import chess.database.vanillajdbc.dao.JdbcBoardDao;
import chess.database.vanillajdbc.dao.JdbcGameDao;

public class DaoConfig {

    public static JdbcGameDao getGameDao() {
        return new JdbcGameDao();
    }

    public static BoardDao getBoardDao() {
        return new JdbcBoardDao();
    }

}
