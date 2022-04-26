package chess.dao;

import java.util.List;

import chess.dto.BoardDto;

public interface BoardDao {

	List<BoardDto> findByGameId(int gameId);

	void save(List<BoardDto> boardDtos, int gameId);

	void update(BoardDto boardDto, int gameId);
}
