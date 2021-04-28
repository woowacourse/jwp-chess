package chess.dto;

import chess.domain.game.ChessGameEntity;

public class PlayingChessgameEntityDto {

    private Long chessGameId;
    private String title;
    private String state;

    public PlayingChessgameEntityDto(ChessGameEntity chessGameEntity) {
        this.chessGameId = chessGameEntity.getId();
        this.title = chessGameEntity.getTitle();
        this.state = chessGameEntity.getState();
    }

    public Long getChessGameId() {
        return chessGameId;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }

}
