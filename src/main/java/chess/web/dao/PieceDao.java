package chess.web.dao;

import chess.web.dto.board.PieceDto;
import java.util.List;

public interface PieceDao {

    void save(int gameId, PieceDto pieceDto);

    void updateByGameId(int gameId, PieceDto pieceDto);

    List<PieceDto> findAllByGameId(int gameId);

    void deleteAllByGameId(int gameId);
}
