package chess.dto;

import chess.entity.ChessGameEntity;

public class ChessRoomDto {

    private final long id;
    private final String name;

    public ChessRoomDto(final ChessGameEntity chessGameEntity) {
        this.id = chessGameEntity.getId();
        this.name = chessGameEntity.getName();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
