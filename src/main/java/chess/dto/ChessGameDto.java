package chess.dto;

public class ChessGameDto {
    private PiecesDto piecesDto;
    private GameResultDto gameResultDto;
    private String gameId;

    public ChessGameDto() {
    }

    public ChessGameDto(PiecesDto piecesDto, String gameId) {
        this.piecesDto = piecesDto;
        this.gameId = gameId;
    }

    public ChessGameDto(GameResultDto gameResultDto, String gameId) {
        this.gameResultDto = gameResultDto;
        this.gameId = gameId;
    }

    public ChessGameDto(PiecesDto piecesDto, GameResultDto gameResultDto, String gameId) {
        this.piecesDto = piecesDto;
        this.gameResultDto = gameResultDto;
        this.gameId = gameId;
    }

    public PiecesDto getPiecesDto() {
        return piecesDto;
    }

    public GameResultDto getGameResultDto() {
        return gameResultDto;
    }

    public String getGameId() {
        return gameId;
    }
}
