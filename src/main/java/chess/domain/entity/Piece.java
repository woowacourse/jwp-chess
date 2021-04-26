package chess.domain.entity;

public class Piece implements Entity<Long> {

    private Long id;
    private Long gameId;
    private String symbol;
    private String position;

    public Piece() {
    }

    public Piece(Long id, Long gameId, String symbol, String position) {
        this.id = id;
        this.gameId = gameId;
        this.symbol = symbol;
        this.position = position;
    }

    public Piece(Long gameId, String symbol, String position) {
        this.gameId = gameId;
        this.symbol = symbol;
        this.position = position;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
