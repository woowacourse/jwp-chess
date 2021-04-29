package chess.dto.game;

import chess.domain.ChessGame;
import chess.domain.Team;
import chess.dto.player.PlayersDTO;
import lombok.Getter;

@Getter
public final class StatusDTO {
    private final String turn;
    private final Double blackScore;
    private final Double whiteScore;
    private final boolean ends;
    private final String winner;
    private final String loser;

    public StatusDTO(final ChessGame chessGame, final PlayersDTO users) {
        this.turn = chessGame.turn().name();
        this.blackScore = chessGame.scoreByTeam(Team.BLACK);
        this.whiteScore = chessGame.scoreByTeam(Team.WHITE);
        this.ends = chessGame.isPlaying();
        this.winner = winner(chessGame.winner(), users);
        this.loser = loser(users);
    }

    private String winner(final Team winner, final PlayersDTO users) {
        if (Team.WHITE.equals(winner)) {
            return users.getWhiteUser();
        }
        if (Team.BLACK.equals(winner)) {
            return users.getBlackUser();
        }
        return "NONE";
    }

    private String loser(final PlayersDTO users) {
        if (users.getBlackUser().equals(winner)) {
            return users.getWhiteUser();
        }
        if (users.getWhiteUser().equals(winner)) {
            return users.getBlackUser();
        }
        return "NONE";
    }
}
