package wooteco.chess.service;

import java.sql.SQLException;

import wooteco.chess.dao.PlayerDao;
import wooteco.chess.dto.PlayerDto;

public class PlayerService {
	private final PlayerDao playerDao = new PlayerDao();

	public int create(PlayerDto playerDto) throws SQLException {
		return playerDao.create(playerDto.getName(), playerDto.getPassword(), playerDto.getTeam());
	}
}
