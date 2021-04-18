package chess.dto.chess;

import chess.dto.game.GameResponseDto;
import chess.dto.piece.PieceDto;
import chess.dto.user.UserResponseDto;
import java.util.List;

public class ChessResponseDto {

    private final List<PieceDto> pieceDtos;
    private final UserResponseDto host;
    private final UserResponseDto guest;
    private final GameResponseDto gameResponseDto;

    public ChessResponseDto(List<PieceDto> pieceDtos, UserResponseDto host, UserResponseDto guest,
        GameResponseDto gameResponseDto) {
        this.pieceDtos = pieceDtos;
        this.host = host;
        this.guest = guest;
        this.gameResponseDto = gameResponseDto;
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
}
