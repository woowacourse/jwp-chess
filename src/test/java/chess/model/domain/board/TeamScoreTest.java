package chess.model.domain.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.model.domain.piece.Piece;
import chess.model.domain.piece.PieceFactory;
import chess.model.domain.piece.Team;
import chess.model.domain.piece.Type;
import com.sun.tools.javac.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TeamScoreTest {

    @Test
    @DisplayName("Null이 생성자에 들어갔을 때 예외 발생")
    void of_WHEN_null() {
        assertAll(
            () -> assertThatThrownBy(() -> TeamScore.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Null"),
            () -> assertThatThrownBy(() -> TeamScore.of(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Null")
        );
    }

    @DisplayName("생성자 테스트")
    @Test
    void of_WHEN_ParameterTeamScore() {
        Map<Team, Double> teamScore = new HashMap<>();
        teamScore.put(Team.BLACK, 1.0);
        teamScore.put(Team.WHITE, 3.0);

        assertThat(TeamScore.of(teamScore).getTeamScore()).isEqualTo(teamScore);
    }

    @DisplayName("생성자 테스트_왕이 없을 경우 0점처리")
    @Test
    void of_WHEN_noKing_THEN_returnZero() {
        Collection<Piece> pieces = new ArrayList<>();
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.QUEEN));
        Map<Team, Integer> pawnSameFileCountByTeam = new HashMap<>();
        TeamScore teamScore = TeamScore.of(pieces, pawnSameFileCountByTeam);

        assertAll(
            () -> assertThat(teamScore.findScore(Team.BLACK)).isEqualTo(0.0),
            () -> assertThat(teamScore.findScore(Team.WHITE)).isEqualTo(0.0)
        );
    }

    @DisplayName("생성자 테스트_Piece를 더한 값이 들어가짐")
    @Test
    void of_WHEN_givenPiece_THEN_sumScore() {
        Collection<Piece> pieces = new ArrayList<>();
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.QUEEN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.KING));
        pieces.add(PieceFactory.getPiece(Team.WHITE, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.WHITE, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.WHITE, Type.KING));

        Map<Team, Integer> pawnSameFileCountByTeam = new HashMap<>();
        TeamScore teamScore = TeamScore.of(pieces, pawnSameFileCountByTeam);

        assertAll(
            () -> assertThat(teamScore.findScore(Team.BLACK)).isEqualTo(10.0),
            () -> assertThat(teamScore.findScore(Team.WHITE)).isEqualTo(2.0)
        );
    }

    @DisplayName("같은 파일을 공유하는 폰의 개수는 1개일 수 없음")
    @Test
    void of_WHEN_sameFilePawnEqualOne_THEN_Exception() {
        Collection<Piece> pieces = new ArrayList<>();
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.KING));
        Map<Team, Integer> pawnSameFileCountByTeam = new HashMap<>();
        pawnSameFileCountByTeam.put(Team.BLACK, 1);

        assertThatThrownBy(() -> TeamScore.of(pieces, pawnSameFileCountByTeam))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("개수");
    }

    @DisplayName("생성자 테스트_파일의 폰 수가 가지고 있는 폰의 수를 넘어가는 경우 예외 발생")
    @Test
    void of_WHEN_sameFilePawnCountMoreThanPawnCount_THEN_Exception() {
        Collection<Piece> pieces = new ArrayList<>();
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.KING));
        Map<Team, Integer> pawnSameFileCountByTeam = new HashMap<>();
        pawnSameFileCountByTeam.put(Team.BLACK, 2);

        assertThatThrownBy(() -> TeamScore.of(pieces, pawnSameFileCountByTeam))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("개수");
    }

    @DisplayName("생성자 테스트_같은 파일에 폰이 2개 이상 있는 경우, 0.5점 처리")
    @Test
    void of_WHEN_sameFilePawn_THEN_halfScore() {
        Collection<Piece> pieces = new ArrayList<>();
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.BLACK, Type.KING));
        pieces.add(PieceFactory.getPiece(Team.WHITE, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.WHITE, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.WHITE, Type.PAWN));
        pieces.add(PieceFactory.getPiece(Team.WHITE, Type.KING));
        Map<Team, Integer> pawnSameFileCountByTeam = new HashMap<>();
        pawnSameFileCountByTeam.put(Team.BLACK, 2);
        TeamScore teamScore = TeamScore.of(pieces, pawnSameFileCountByTeam);

        assertAll(
            () -> assertThat(teamScore.findScore(Team.BLACK)).isEqualTo(2.0),
            () -> assertThat(teamScore.findScore(Team.WHITE)).isEqualTo(3.0)
        );
    }

    @Test
    void findWinners() {
        Map<Team, Double> blackWinScores = new HashMap<>();
        blackWinScores.put(Team.BLACK, 10.0);
        blackWinScores.put(Team.WHITE, 9.0);
        TeamScore blackWinTeamScore = TeamScore.of(blackWinScores);

        Map<Team, Double> whiteWinScores = new HashMap<>();
        whiteWinScores.put(Team.BLACK, 1.0);
        whiteWinScores.put(Team.WHITE, 3.0);
        TeamScore whiteWinTeamScore = TeamScore.of(whiteWinScores);

        Map<Team, Double> allWinScores = new HashMap<>();
        allWinScores.put(Team.BLACK, 15.0);
        allWinScores.put(Team.WHITE, 15.0);
        TeamScore allWinTeamScore = TeamScore.of(allWinScores);

        assertAll(
            () -> assertThat(blackWinTeamScore.findWinners()).isEqualTo(List.of(Team.BLACK)),
            () -> assertThat(whiteWinTeamScore.findWinners()).isEqualTo(List.of(Team.WHITE)),
            () -> assertThat(allWinTeamScore.findWinners()).contains(Team.WHITE, Team.BLACK)
        );
    }

    @Test
    void isDraw() {
        Map<Team, Double> blackWinScores = new HashMap<>();
        blackWinScores.put(Team.BLACK, 10.0);
        blackWinScores.put(Team.WHITE, 9.0);
        TeamScore blackWinTeamScore = TeamScore.of(blackWinScores);

        Map<Team, Double> whiteWinScores = new HashMap<>();
        whiteWinScores.put(Team.BLACK, 1.0);
        whiteWinScores.put(Team.WHITE, 3.0);
        TeamScore whiteWinTeamScore = TeamScore.of(whiteWinScores);

        Map<Team, Double> allWinScores = new HashMap<>();
        allWinScores.put(Team.BLACK, 15.0);
        allWinScores.put(Team.WHITE, 15.0);
        TeamScore allWinTeamScore = TeamScore.of(allWinScores);

        assertAll(
            () -> assertThat(blackWinTeamScore.isDraw()).isFalse(),
            () -> assertThat(whiteWinTeamScore.isDraw()).isFalse(),
            () -> assertThat(allWinTeamScore.isDraw()).isTrue()
        );
    }

    @Test
    void isNotDraw() {
        Map<Team, Double> blackWinScores = new HashMap<>();
        blackWinScores.put(Team.BLACK, 10.0);
        blackWinScores.put(Team.WHITE, 9.0);
        TeamScore blackWinTeamScore = TeamScore.of(blackWinScores);

        Map<Team, Double> whiteWinScores = new HashMap<>();
        whiteWinScores.put(Team.BLACK, 1.0);
        whiteWinScores.put(Team.WHITE, 3.0);
        TeamScore whiteWinTeamScore = TeamScore.of(whiteWinScores);

        Map<Team, Double> allWinScores = new HashMap<>();
        allWinScores.put(Team.BLACK, 15.0);
        allWinScores.put(Team.WHITE, 15.0);
        TeamScore allWinTeamScore = TeamScore.of(allWinScores);

        assertAll(
            () -> assertThat(blackWinTeamScore.isNotDraw()).isTrue(),
            () -> assertThat(whiteWinTeamScore.isNotDraw()).isTrue(),
            () -> assertThat(allWinTeamScore.isNotDraw()).isFalse()
        );
    }

    @Test
    void isWin() {
        Map<Team, Double> blackWinScores = new HashMap<>();
        blackWinScores.put(Team.BLACK, 10.0);
        blackWinScores.put(Team.WHITE, 9.0);
        TeamScore blackWinTeamScore = TeamScore.of(blackWinScores);

        Map<Team, Double> whiteWinScores = new HashMap<>();
        whiteWinScores.put(Team.BLACK, 1.0);
        whiteWinScores.put(Team.WHITE, 3.0);
        TeamScore whiteWinTeamScore = TeamScore.of(whiteWinScores);

        Map<Team, Double> allWinScores = new HashMap<>();
        allWinScores.put(Team.BLACK, 15.0);
        allWinScores.put(Team.WHITE, 15.0);
        TeamScore allWinTeamScore = TeamScore.of(allWinScores);

        assertAll(
            () -> assertThat(blackWinTeamScore.isWin(Team.BLACK)).isTrue(),
            () -> assertThat(blackWinTeamScore.isWin(Team.WHITE)).isFalse(),
            () -> assertThat(whiteWinTeamScore.isWin(Team.BLACK)).isFalse(),
            () -> assertThat(whiteWinTeamScore.isWin(Team.WHITE)).isTrue(),
            () -> assertThat(allWinTeamScore.isWin(Team.BLACK)).isFalse(),
            () -> assertThat(allWinTeamScore.isWin(Team.WHITE)).isFalse()
        );
    }

    @Test
    void isLose() {
        Map<Team, Double> blackWinScores = new HashMap<>();
        blackWinScores.put(Team.BLACK, 10.0);
        blackWinScores.put(Team.WHITE, 9.0);
        TeamScore blackWinTeamScore = TeamScore.of(blackWinScores);

        Map<Team, Double> whiteWinScores = new HashMap<>();
        whiteWinScores.put(Team.BLACK, 1.0);
        whiteWinScores.put(Team.WHITE, 3.0);
        TeamScore whiteWinTeamScore = TeamScore.of(whiteWinScores);

        Map<Team, Double> allWinScores = new HashMap<>();
        allWinScores.put(Team.BLACK, 15.0);
        allWinScores.put(Team.WHITE, 15.0);
        TeamScore allWinTeamScore = TeamScore.of(allWinScores);

        assertAll(
            () -> assertThat(blackWinTeamScore.isLose(Team.BLACK)).isFalse(),
            () -> assertThat(blackWinTeamScore.isLose(Team.WHITE)).isTrue(),
            () -> assertThat(whiteWinTeamScore.isLose(Team.BLACK)).isTrue(),
            () -> assertThat(whiteWinTeamScore.isLose(Team.WHITE)).isFalse(),
            () -> assertThat(allWinTeamScore.isLose(Team.BLACK)).isFalse(),
            () -> assertThat(allWinTeamScore.isLose(Team.WHITE)).isFalse()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"10.0, 9.0", "9.0, 10.0", "10.0, 10.0"})
    void findScore(Double blackScore, Double whiteScore) {
        Map<Team, Double> blackWinScores = new HashMap<>();
        blackWinScores.put(Team.BLACK, blackScore);
        blackWinScores.put(Team.WHITE, whiteScore);
        TeamScore teamScore = TeamScore.of(blackWinScores);

        assertAll(
            () -> assertThat(teamScore.findScore(Team.BLACK)).isEqualTo(blackScore),
            () -> assertThat(teamScore.findScore(Team.WHITE)).isEqualTo(whiteScore)
        );
    }
}
