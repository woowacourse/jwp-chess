package chess.web.dao;

import chess.web.dto.PieceDto;
import java.util.List;

public interface PieceDao2 {

    void save(int gameId, PieceDto pieceDto);

    void updateByGameId(int gameId, PieceDto pieceDto);

    List<PieceDto> findAllByGameId(int gameId);

    void deleteAllByGameId(int gameId);
}
