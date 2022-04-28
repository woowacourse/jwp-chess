package chess.repository;

import java.util.List;

import chess.dto.ChessGameDto;
import chess.entity.ChessGameEntity;
import chess.model.state.State;

public interface GamesRepository {

    void save(ChessGameDto chessGameDto);

    List<ChessGameEntity> getGames();

    void delete(Long id);

    ChessGameEntity getGame(Long id);

    State getState(Long id);
}
