package chess.repository;

import java.util.List;

import chess.dto.ChessGameDto;
import chess.entity.ChessGameEntity;

public interface GamesRepository {

    void save(ChessGameDto chessGameDto);

    List<ChessGameEntity> getGames();
}
