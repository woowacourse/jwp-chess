package chess.dto;

import java.util.List;

public class ChessResponseDto {

    private final Long gameId;
    private final List<PieceDto> pieces;
    private final String turn;
    private final String status;

    public ChessResponseDto(Long gameId, List<PieceDto> pieces, String turn, String status) {
        this.gameId = gameId;
        this.pieces = pieces;
        this.turn = turn;
        this.status = status;
    }

    public static ChessResponseDto from(Long gameId, List<PieceDto> pieces, GameStatusDto gameDto) {
        return new ChessResponseDto(gameId, pieces, gameDto.getTurn(), gameDto.getStatus());
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }

    public String getTurn() {
        return turn;
    }

    public String getStatus() {
        return status;
    }
}
