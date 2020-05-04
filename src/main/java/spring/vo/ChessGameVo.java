package spring.vo;

import spring.entity.ChessGameEntity;

public class ChessGameVo {
    private final Long id;
    private final String name;

    public ChessGameVo(ChessGameEntity chessGameEntity) {
        this.id = chessGameEntity.getId();
        this.name = chessGameEntity.getGameName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
