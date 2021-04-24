package chess.dto.game;

import chess.domain.ChessGame;
import chess.domain.Team;
import chess.dto.user.UsersDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class GameDTO {
    private final String id;
    private final UsersDTO users;
    private final List<PieceDTO> pieces;
    private final String button;
    private final boolean white;
    private final Double blackScore;
    private final Double whiteScore;
    private final String error;

    public GameDTO(final String id, final UsersDTO users, final String button, final boolean white) {
        this(id, users, Collections.emptyList(), button, white, 38.0, 38.0, "");
    }

    public GameDTO(final String id, final UsersDTO users, final ChessGame chessGame, final String button) {
        this(id, users, PiecesDTO.create(chessGame.board()).toList(), button,
                Team.WHITE.equals(chessGame.turn()), chessGame.scoreByTeam(Team.BLACK),
                chessGame.scoreByTeam(Team.WHITE), "");
    }

    public GameDTO(final String id, final String button, final UsersDTO users, final String message) {
        this(id, users, Collections.emptyList(), button, true, 38.0, 38.0, message);
    }
}
