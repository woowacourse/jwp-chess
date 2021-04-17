package chess.dto;

import java.util.List;

public class ChessResponseDto {

    private final List<PieceDto> pieceDtos;
    private final UserResponseDto host;
    private final UserResponseDto guest;
    private final GameResponseDto gameResponseDto;
    private final double blackScore;
    private final double whiteScore;

    public ChessResponseDto(List<PieceDto> pieceDtos, UserResponseDto host, UserResponseDto guest, GameResponseDto gameResponseDto, double blackScore, double whiteScore) {
        this.pieceDtos = pieceDtos;
        this.host = host;
        this.guest = guest;
        this.gameResponseDto = gameResponseDto;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public List<PieceDto> getPieceDtos() {
        return pieceDtos;
    }

    public UserResponseDto getHost() {
        return host;
    }

    public UserResponseDto getGuest() {
        return guest;
    }

    public GameResponseDto getGameResponseDto() {
        return gameResponseDto;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }
}
