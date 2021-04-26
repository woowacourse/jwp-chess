package chess.dto.chess;

import chess.dto.game.GameResponseDto;
import chess.dto.piece.PieceResponseDto;
import chess.dto.user.UserResponseDto;
import java.util.List;

public class ChessResponseDto {

    private final List<PieceResponseDto> pieceResponseDtos;
    private final UserResponseDto host;
    private final UserResponseDto guest;
    private final GameResponseDto gameResponseDto;

    public ChessResponseDto(final List<PieceResponseDto> pieceResponseDtos,
        final UserResponseDto host,
        final UserResponseDto guest, final GameResponseDto gameResponseDto) {

        this.pieceResponseDtos = pieceResponseDtos;
        this.host = host;
        this.guest = guest;
        this.gameResponseDto = gameResponseDto;
    }

    public List<PieceResponseDto> getPieceDtos() {
        return pieceResponseDtos;
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
