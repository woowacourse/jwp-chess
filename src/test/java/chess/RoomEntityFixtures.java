package chess;

import static chess.domain.GameStatus.READY;
import static chess.domain.chesspiece.Color.WHITE;

import chess.entity.RoomEntity;

public class RoomEntityFixtures {

    public static final RoomEntity generateRoomEntity() {
        return new RoomEntity(0, "매트의 체스", "123123", READY.getValue(), WHITE.getValue());
    }
}
