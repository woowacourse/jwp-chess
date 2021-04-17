package chess.dto.response;

import chess.dto.PieceDto;

public class PieceResponseDto {

    private final String position;
    private final String name;

    public PieceResponseDto(PieceDto pieceDto) {
        this.position = pieceDto.getPosition();
        this.name = pieceDto.getPieceName();
    }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}
