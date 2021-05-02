package chess.service.game;

import dto.ChessGameDto;
import dto.MoveDto;

public interface ChessGameService {
    ChessGameDto move(Long gameId, MoveDto moveDto);
}
