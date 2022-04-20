package chess.dao;

import chess.dto.BoardDto;
import java.util.List;

public interface BoardDao {

    List<BoardDto> findByGameId(int gameId);

    void save(List<BoardDto> boardDtos, int gameId);

    void update(BoardDto boardDto, int gameId);
}
