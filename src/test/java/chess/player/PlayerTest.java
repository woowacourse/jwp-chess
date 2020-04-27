package chess.player;

import spring.chess.board.ChessBoard;
import spring.chess.board.ChessBoardCreater;
import spring.chess.game.ChessSet;
import spring.chess.location.Location;
import spring.chess.piece.type.Piece;
import spring.chess.score.Score;
import spring.chess.team.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.chess.player.Player;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    @Test
    @DisplayName("팀이 같은지 : 참")
    void isSame1() {
        Player player = new Player(new ChessSet(Collections.EMPTY_MAP), Team.WHITE);

        assertThat(player.is(Team.WHITE)).isTrue();
    }

    @Test
    @DisplayName("팀이 같은지 : 거짓")
    void isSame2() {
        Player player = new Player(new ChessSet(Collections.EMPTY_MAP), Team.WHITE);

        assertThat(player.is(Team.BLACK)).isFalse();
    }

    @Test
    @DisplayName("팀이 같지 않은지 : 참")
    void isNotSame() {
        Player player = new Player(new ChessSet(Collections.EMPTY_MAP), Team.WHITE);

        assertThat(player.isNotSame(Team.BLACK)).isTrue();
    }

    @Test
    @DisplayName("팀이 같지 않은지 : 거짓")
    void isNotSame2() {
        Player player = new Player(new ChessSet(Collections.EMPTY_MAP), Team.WHITE);

        assertThat(player.isNotSame(Team.WHITE)).isFalse();
    }

    @Test
    void calculateScoreExceptPawnReduce() {
        ChessBoard chessBoard = ChessBoardCreater.create();
        Map<Location, Piece> locationPieceMap = chessBoard.giveMyPieces(Team.WHITE);
        Player player = new Player(new ChessSet(locationPieceMap), Team.WHITE);

        assertThat(player.calculateScoreExceptPawnReduce()).isEqualTo(new Score(38));
    }

}