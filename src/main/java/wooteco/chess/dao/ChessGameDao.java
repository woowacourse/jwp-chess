package wooteco.chess.dao;

import java.util.List;
import java.util.Optional;

import wooteco.chess.domain.game.ChessGame;

public interface ChessGameDao {
	int create() throws Exception;

	List<Integer> findAll() throws Exception;

	Optional<ChessGame> findById(int id) throws Exception;

	void updateById(int id, ChessGame chessGame) throws Exception;

	void deleteById(int id) throws Exception;
}
