package chess.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.chess.board.ChessBoard;
import spring.chess.board.ChessBoardCreater;
import spring.chess.location.Location;
import spring.chess.piece.type.*;
import spring.chess.team.Team;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ChessBoardTest {
    @DisplayName("생성자 테스트")
    @Test
    void name() {
        ChessBoard chessBoard = ChessBoardCreater.create();
        assertThat(chessBoard).isInstanceOf(ChessBoard.class);
    }

    @DisplayName("팀에 따른 모든 말들을 가져온다.")
    @Test
    void giveMyPiece() {
        ChessBoard chessBoard = ChessBoardCreater.create();
        Map<Location, Piece> givenPieces = chessBoard.giveMyPieces(Team.BLACK);

        Map<Location, Piece> actual = new HashMap<>();
        putNoble(actual, 8);
        putPawns(actual, 7);

        assertThat(givenPieces).isEqualTo(actual);
    }

    private void putNoble(Map<Location, Piece> board, int row) {
        board.put(new Location(row, 'a'), new Rook(Team.BLACK));
        board.put(new Location(row, 'b'), new Knight(Team.BLACK));
        board.put(new Location(row, 'c'), new Bishop(Team.BLACK));
        board.put(new Location(row, 'd'), new Queen(Team.BLACK));
        board.put(new Location(row, 'e'), new King(Team.BLACK));
        board.put(new Location(row, 'f'), new Bishop(Team.BLACK));
        board.put(new Location(row, 'g'), new Knight(Team.BLACK));
        board.put(new Location(row, 'h'), new Rook(Team.BLACK));
    }

    private void putPawns(Map<Location, Piece> board, int row) {
        for (int i = 0; i < 8; i++) {
            board.put(new Location(row, (char) (i + 'a')), new Pawn(Team.BLACK));
        }
    }
}