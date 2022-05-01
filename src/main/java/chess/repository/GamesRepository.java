package chess.repository;

import java.util.List;

import chess.dto.ChessGameDto;
import chess.model.ChessGame;
import chess.model.state.State;

public interface GamesRepository {

    void save(ChessGameDto chessGameDto);

    List<ChessGame> getGames();

    void delete(Long id);

    ChessGame getGame(Long id);

    State getState(Long id);
}
