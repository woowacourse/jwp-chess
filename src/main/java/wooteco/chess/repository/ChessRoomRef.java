package wooteco.chess.repository;

import org.springframework.data.relational.core.mapping.Table;

@Table("CommandsChessRoom")
public class ChessRoomRef {
    private Long chessRoom;

    public ChessRoomRef() {
    }

    public ChessRoomRef(ChessRoom chessRoom) {
        this.chessRoom = chessRoom.getRoomId();
    }
}
