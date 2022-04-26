package chess.dao;

import java.util.List;

import chess.dto.ChessGameDto;
import chess.entity.ChessGameEntity;

public interface ChessGameDao {

    void insert(final ChessGameDto chessGameDto);

    ChessGameEntity find(final Long id);

    int delete(final Long id);

    List<ChessGameEntity> findAll();
}
