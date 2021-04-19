package chess.dto;

public class ChessGameStatusDto {

    private final Long chessGameId;
    private final boolean exist;

    private ChessGameStatusDto(boolean exist) {
        this(null, exist);
    }

    private ChessGameStatusDto(Long chessGameId, boolean exist) {
        this.chessGameId = chessGameId;
        this.exist = exist;
    }

    public static ChessGameStatusDto exist(Long chessGameId) {
        return new ChessGameStatusDto(chessGameId, true);
    }

    public static ChessGameStatusDto isNotExist() {
        return new ChessGameStatusDto(false);
    }

    public Long getChessGameId() {
        return chessGameId;
    }

    public boolean isExist() {
        return exist;
    }

}
