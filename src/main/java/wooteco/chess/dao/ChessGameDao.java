package wooteco.chess.dao;

import wooteco.chess.entity.ChessGameEntity;

public interface ChessGameDao {

	long add(final ChessGameEntity entity);

	long findMaxGameId();

	boolean isEmpty();

	void deleteAll();

}
