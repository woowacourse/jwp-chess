package chess.game;

import spring.chess.board.ChessBoard;
import spring.chess.board.ChessBoardCreater;
import spring.chess.location.Location;
import spring.chess.piece.type.Pawn;
import spring.chess.piece.type.Piece;
import spring.chess.score.Score;
import spring.chess.team.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.chess.game.ChessSet;

import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChessSetTest {
    @Test
    void calculateScoreExceptPawnReduce() {
        ChessBoard chessBoard = ChessBoardCreater.create();
        ChessSet chessSet = new ChessSet(chessBoard.giveMyPieces(Team.WHITE));
        assertThat(chessSet.calculateScoreExceptPawnReduce()).isEqualTo(new Score(38));
    }

    @Test
    void getTeam() {
        HashMap<Location, Piece> locationPieceHashMap = new HashMap();
        locationPieceHashMap.put(new Location(1, 'a'), new Pawn(Team.WHITE));
        ChessSet chessSet = new ChessSet(locationPieceHashMap);

        assertThat(chessSet.getTeam()).isEqualTo(Team.WHITE);
    }

    @Test
    @DisplayName("null인 value를 만났을 때")
    void getTeam2() {
        HashMap<Location, Piece> locationPieceHashMap = new HashMap();
        locationPieceHashMap.put(new Location(1, 'a'), null);
        ChessSet chessSet = new ChessSet(locationPieceHashMap);

        assertThatThrownBy(() -> {
            chessSet.getTeam();
        }).isInstanceOf(NoSuchElementException.class);
    }
}