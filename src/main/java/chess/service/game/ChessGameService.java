package chess.service.game;

import chess.dto.ChessGameDto;
import chess.dto.request.GameMoveRequest;

public interface ChessGameService {
    ChessGameDto move(Long gameId, GameMoveRequest moveDto);
}
