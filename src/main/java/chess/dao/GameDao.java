package chess.dao;

import chess.dto.GameDto;
import java.util.List;

public interface GameDao {

	int save(GameDto gameDto);

	String findStateById(int gameId);

	void update(String state, int gameId);

	void deleteById(int gameId);

	String findPasswordById(int gameId);

	List<GameDto> findGames();
}
