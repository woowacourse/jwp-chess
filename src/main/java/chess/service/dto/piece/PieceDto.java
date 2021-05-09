package chess.service.dto.piece;

public class PieceDto {

    private Long id;
    private Long gameId;
    private String position;
    private String symbol;

    public PieceDto(Long gameId, String position, String symbol) {
        this(null, gameId, position, symbol);
    }

    public PieceDto(Long id, Long gameId, String position, String symbol) {
        this.id = id;
        this.gameId = gameId;
        this.position = position;
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getPosition() {
        return position;
    }

    public String getSymbol() {
        return symbol;
    }
}
