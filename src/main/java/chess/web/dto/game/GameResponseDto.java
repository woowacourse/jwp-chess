package chess.web.dto.game;

import chess.domain.game.Game;
import chess.domain.game.team.Team;
import chess.domain.user.User;
import chess.web.dto.game.piece.PieceResponseDto;
import chess.web.dto.user.UserResponseDto;
import java.util.List;
import java.util.stream.Collectors;

public class GameResponseDto {

    private final String name;
    private final Team turn;
    private final boolean finished;
    private final List<PieceResponseDto> pieceResponseDtos;
    private final UserResponseDto host;
    private final UserResponseDto guest;

    private GameResponseDto(final String name, final Team turn, final boolean finished,
        final List<PieceResponseDto> pieceResponseDtos, final UserResponseDto host,
        final UserResponseDto guest) {

        this.name = name;
        this.turn = turn;
        this.finished = finished;
        this.pieceResponseDtos = pieceResponseDtos;
        this.host = host;
        this.guest = guest;
    }

    public static GameResponseDto of(final Game game, final User host, final User guest) {
        final List<PieceResponseDto> pieceResponseDtos =
            game.toPieces()
                .stream()
                .map(PieceResponseDto::from)
                .collect(Collectors.toList());

        return new GameResponseDto(
            game.getRoomName(),
            game.getTurn(),
            game.isFinished(),
            pieceResponseDtos,
            UserResponseDto.from(host),
            UserResponseDto.from(guest)
        );
    }

    public String getName() {
        return name;
    }

    public Team getTurn() {
        return turn;
    }

    public boolean isFinished() {
        return finished;
    }

    public List<PieceResponseDto> getPieceResponseDtos() {
        return pieceResponseDtos;
    }

    public UserResponseDto getHost() {
        return host;
    }

    public UserResponseDto getGuest() {
        return guest;
    }

}
