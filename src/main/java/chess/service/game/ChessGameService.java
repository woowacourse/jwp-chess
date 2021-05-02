package chess.service.game;

import chess.dto.ChessGameDto;
import chess.dto.MoveDto;

public interface ChessGameService {
    ChessGameDto move(Long gameId, MoveDto moveDto);
}
