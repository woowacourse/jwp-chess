package chess.spring.controller.dto.response;

import chess.spring.domain.ChessRoom;
import org.springframework.stereotype.Repository;

@Repository
public interface ChessRoomRepository {

    ChessRoom findNotFullChessRooms();
}
