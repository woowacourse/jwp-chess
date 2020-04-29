package wooteco.chess.service.spark;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import wooteco.chess.db.dao.PlayerDao;
import wooteco.chess.db.entity.PlayerEntity;

@Service
public class SparkPlayerService {
	private final PlayerDao playerDao;

	public SparkPlayerService(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	public int save(PlayerEntity playerEntity) throws SQLException {
		return playerDao.save(playerEntity.getName(), playerEntity.getPassword(), playerEntity.getTeam());
	}
}
