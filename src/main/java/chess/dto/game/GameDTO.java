package chess.dto.game;

import chess.domain.ChessGame;
import chess.domain.Team;
import chess.dto.user.UsersDTO;

import java.util.Collections;
import java.util.List;

public class GameDTO {
    private final String id;
    private final UsersDTO users;
    private final List<PieceDTO> pieces;
    private final String button;
    private final boolean white;
    private final Double blackScore;
    private final Double whiteScore;
    private final String error;

    public GameDTO(final String id, final UsersDTO users, final List<PieceDTO> pieces, final String button,
                   final boolean white, final Double blackScore, final Double whiteScore, final String error) {
        this.id = id;
        this.users = users;
        this.pieces = pieces;
        this.button = button;
        this.white = white;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
        this.error = error;
    }

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

    public String getId() {
        return id;
    }

    public UsersDTO getUsers() {
        return users;
    }

    public List<PieceDTO> getPieces() {
        return pieces;
    }

    public String getButton() {
        return button;
    }

    public boolean isWhite() {
        return white;
    }

    public Double getBlackScore() {
        return blackScore;
    }

    public Double getWhiteScore() {
        return whiteScore;
    }

    public String getError() {
        return error;
    }
}
