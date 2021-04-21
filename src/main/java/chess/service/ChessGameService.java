package chess.service;

import chess.domain.Room;
import dto.ChessGameDto;
import dto.MoveDto;
import dto.RoomRequestDto;
import org.springframework.http.ResponseEntity;

public interface ChessGameService {
    ChessGameDto move(Long gameId, MoveDto moveDto);
}
