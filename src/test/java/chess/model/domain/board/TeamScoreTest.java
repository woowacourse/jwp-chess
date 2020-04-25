package chess.model.domain.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.model.domain.piece.King;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Rook;
import chess.model.domain.piece.Team;
import chess.model.domain.state.MoveInfo;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TeamScoreTest {

    @Test
    @DisplayName("Null이 생성자에 들어갔을 때 예외 발생")
    void validNotNull() {
        assertThatThrownBy(() -> new TeamScore(null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Null");
    }

    @Test
    @DisplayName("게임 점수 계산")
    void calculateScore() {
        ChessGame chessGame = new ChessGame();
        TeamScore teamScore = chessGame.deriveTeamScoreFrom();
        Map<Team, Double> teamScores = teamScore.getTeamScore();
        assertThat(teamScores.get(Team.BLACK)).isEqualTo(38);
        assertThat(teamScores.get(Team.WHITE)).isEqualTo(38);

        chessGame.move(new MoveInfo("c2", "c4"));
        chessGame.move(new MoveInfo("d7", "d5"));
        chessGame.move(new MoveInfo("c4", "d5"));

        teamScore = chessGame.deriveTeamScoreFrom();
        teamScores = teamScore.getTeamScore();
        assertThat(teamScores.get(Team.BLACK)).isEqualTo(37);
        assertThat(teamScores.get(Team.WHITE)).isEqualTo(37);
    }

    @Test
    @DisplayName("승자 구하기")
    void getWinnerByScore() {
        ChessGame chessGame = new ChessGame();
        TeamScore teamScore = chessGame.deriveTeamScoreFrom();
        assertThat(teamScore.getWinners().size()).isEqualTo(2);

        chessGame.move(new MoveInfo("b1", "c3"));
        chessGame.move(new MoveInfo("d7", "d5"));
        chessGame.move(new MoveInfo("c3", "d5"));

        teamScore = chessGame.deriveTeamScoreFrom();
        assertThat(teamScore.getWinners().size()).isEqualTo(1);
        assertThat(teamScore.getWinners().get(0)).isEqualTo(Team.WHITE);
    }

    @Test
    @DisplayName("킹 잡혔을 때 0점 처리")
    void noKingZero() {
        Map<Square, Piece> boardInitial = new HashMap<>();
        boardInitial.put(Square.of("e1"), King.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("a8"), Rook.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("h8"), Rook.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("a1"), Rook.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("h1"), Rook.getPieceInstance(Team.WHITE));
        ChessGame chessGame = new ChessGame(ChessBoard.of(boardInitial), Team.WHITE,
            CastlingElement.createInitial(), new EnPassant());

        TeamScore teamScore = chessGame.deriveTeamScoreFrom();

        assertThat(teamScore.getWinners().contains(Team.WHITE)).isTrue();
        assertThat(teamScore.getWinners().size()).isEqualTo(1);

        assertThat(teamScore.getTeamScore().get(Team.BLACK)).isEqualTo(0.0);
        assertThat(teamScore.getTeamScore().get(Team.WHITE)).isEqualTo(10.0);
    }
}
