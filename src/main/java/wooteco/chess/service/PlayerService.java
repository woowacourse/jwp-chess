package wooteco.chess.service;

import wooteco.chess.dao.PlayerDao;

import java.sql.SQLException;

public class PlayerService {
    private final PlayerDao playerDao = new PlayerDao();

    public int create(String playerName, String playerPassword, String team) throws SQLException, ClassNotFoundException {
        int id = playerDao.create(playerName, playerPassword, team);
        return id;
    }
}
