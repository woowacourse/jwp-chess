package chess.domain.game;

import static chess.utils.TestFixture.TEST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.score.Scores;
import chess.domain.color.type.TeamColor;
import chess.domain.password.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessGameTest {

    private static final String INITIAL_BOARD_STATUS = ""
        + "RNBQKBNR"
        + "PPPPPPPP"
        + "........"
        + "........"
        + "........"
        + "........"
        + "pppppppp"
        + "rnbqkbnr";
    private static final double INITIAL_SCORE = 38.0;

    private final ChessGame chessGame = new ChessGame(TEST_TITLE);

    @DisplayName("기물 이동 테스트")
    @Test
    void movePiece() {
        assertThat(chessGame.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(chessGame.getBoardStatus()).isEqualTo(INITIAL_BOARD_STATUS);
        assertThat(chessGame.getCurrentTurnTeamColor()).isEqualTo(TeamColor.WHITE);

        String startPositionInput = "c2";
        String destinationInput = "c4";

        chessGame.movePiece(startPositionInput, destinationInput);

        assertThat(chessGame.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(chessGame.getBoardStatus()).isEqualTo(""
            + "RNBQKBNR"
            + "PPPPPPPP"
            + "........"
            + "........"
            + "..p....."
            + "........"
            + "pp.ppppp"
            + "rnbqkbnr");
        assertThat(chessGame.getCurrentTurnTeamColor()).isEqualTo(TeamColor.BLACK);
        Scores scores = chessGame.getScores();
        assertThat(scores.getWhitePlayerScore()).isEqualTo(INITIAL_SCORE);
        assertThat(scores.getBlackPlayerScore()).isEqualTo(INITIAL_SCORE);
    }

    @DisplayName("비밀번호 암호화 테스트")
    @Test
    void encryptPassword() {
        String rawWhitePlayerPassword = "rawWhitePlayerPassword";
        String rawBlackPlayerPassword = "rawBlackPlayerPassword";

        ChessGame chessGame = new ChessGame(TEST_TITLE, rawWhitePlayerPassword);
        chessGame.joinBlackPlayerWithPassword(rawBlackPlayerPassword);

        String encryptedWhitePlayerPassword = PasswordEncoder.encrypt(rawWhitePlayerPassword);
        String encryptedBlackPlayerPassword = PasswordEncoder.encrypt(rawBlackPlayerPassword);

        assertThat(chessGame.getEncryptedWhitePlayerPassword()).isEqualTo(encryptedWhitePlayerPassword);
        assertThat(chessGame.getEncryptedBlackPlayerPassword()).isEqualTo(encryptedBlackPlayerPassword);
    }
}
