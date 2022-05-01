package chess.dao;

import chess.service.dto.ChessGameDto;
import chess.service.dto.GamesDto;
import chess.service.dto.StatusDto;

public interface GameDao {
    void update(ChessGameDto dto);

    ChessGameDto findById(Long id);

    void updateStatus(StatusDto statusDto, Long id);

    GamesDto findAll();

    Long createGame(String name, String password);

    void deleteGame(Long id);

    boolean existsById(Long gameId);

    void removeAll();
}
