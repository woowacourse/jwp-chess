package wooteco.chess.dao;

import java.util.List;

import wooteco.chess.entity.ChessHistoryEntity;

public interface ChessHistoryDao {

	List<ChessHistoryEntity> findAllByGameId(final long gameId);

	void add(final ChessHistoryEntity entity);

	void deleteAll();

}
