package chess.service.game;

import dto.ChessGameDto;
import dto.MoveDto;

public interface ChessGameService {
    ChessGameDto load(Long roomId);
    ChessGameDto move(Long gameId, MoveDto moveDto);
}
