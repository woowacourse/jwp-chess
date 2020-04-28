package wooteco.chess.service.spark;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import wooteco.chess.dao.PlayerDao;
import wooteco.chess.dto.PlayerDto;

@Service
public class SparkPlayerService {
	private final PlayerDao playerDao;

	public SparkPlayerService(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	public int save(PlayerDto playerDto) throws SQLException {
		return playerDao.save(playerDto.getName(), playerDto.getPassword(), playerDto.getTeam());
	}
}
