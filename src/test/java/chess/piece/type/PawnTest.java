package chess.piece.type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.chess.board.ChessBoard;
import spring.chess.board.ChessBoardCreater;
import spring.chess.board.Route;
import spring.chess.location.Location;
import spring.chess.piece.type.Pawn;
import spring.chess.piece.type.Piece;
import spring.chess.piece.type.Rook;
import spring.chess.team.Team;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PawnTest {
    @Test
    @DisplayName("초기 위치의 폰의 2단 이동 테스트")
    void canMove1() {
        Pawn pawn = new Pawn(Team.BLACK);
        Location now = new Location(7, 'a');

        Location moveTwiceForward = new Location(5, 'a');

        Route route = new Route(Collections.emptyMap(), now, moveTwiceForward);
        boolean moveTwiceForwardActual = pawn.canMove(route);
        assertThat(moveTwiceForwardActual).isTrue();
    }

    @Test
    @DisplayName("초기 위치의 폰의 1단 이동 테스트")
    void canMove2() {
        Pawn pawn = new Pawn(Team.BLACK);
        Location now = new Location(7, 'a');
        Location moveOnceForward = new Location(6, 'a');

        Route route = new Route(Collections.emptyMap(), now, moveOnceForward);

        boolean moveOnceForwardActual = pawn.canMove(route);
        assertThat(moveOnceForwardActual).isTrue();
    }

    @Test
    @DisplayName("폰의 대각선 이동 테스트")
    void canMove3() {
        Map<Location, Piece> board = new HashMap<>();
        Pawn pawn = new Pawn(Team.BLACK);
        Location now = new Location(6, 'a');
        Location moveDiagonal = new Location(5, 'b');

        board.put(now, pawn);
        board.put(moveDiagonal, new Rook(Team.WHITE));

        Route route = new Route(board, now, moveDiagonal);

        boolean moveDiagonalActual = pawn.canMove(route);
        assertThat(moveDiagonalActual).isTrue();
    }

    @Test
    @DisplayName("초기 위치의 폰의 이동 테스트")
    void cantMove() {
        Pawn pawn = new Pawn(Team.BLACK);
        Location now = new Location(7, 'a');
        Location cantAfter = new Location(4, 'a');

        Route route = new Route(Collections.emptyMap(), now, cantAfter);

        boolean cantActual = pawn.canMove(route);
        assertThat(cantActual).isFalse();
    }

    @Test
    @DisplayName("초기 위치가 아닌 일반적인 폰의 1단 이동")
    void canMove4() {
        ChessBoard chessBoard = ChessBoardCreater.create();

        Pawn pawn = new Pawn(Team.BLACK);
        Location now = new Location(6, 'a');
        Location moveOnceForward = new Location(5, 'a');

        Route route = new Route(Collections.emptyMap(), now, moveOnceForward);

        boolean moveOnceForwardActual = pawn.canMove(route);
        assertThat(moveOnceForwardActual).isTrue();
    }

    @Test
    @DisplayName("초기 위치가 아닌 일반적인 폰의 2단 이동")
    void cantMove2() {
        ChessBoard chessBoard = ChessBoardCreater.create();

        Pawn pawn = new Pawn(Team.BLACK);

        Location now = new Location(6, 'a');
        Location moveTwiceForward = new Location(4, 'a');

        Route route = new Route(Collections.emptyMap(), now, moveTwiceForward);
        boolean moveTwiceForwardActual = pawn.canMove(route);
        assertThat(moveTwiceForwardActual).isFalse();
    }

    @DisplayName("폰의 대각선 위치에 적이 없는 경우")
    @Test
    void name2() {
        Map<Location, Piece> board = new HashMap<>();
        Pawn givenPiece = new Pawn(Team.BLACK);
        Location now = new Location(7, 'a');
        board.put(now, givenPiece);

        Location destination = new Location(6, 'b');

        Route route = new Route(board, now, destination);

        boolean actual = givenPiece.canMove(route);
        assertThat(actual).isFalse();
    }

    @DisplayName("폰의 두 칸의 직선 위치로 가는 중 적이 있는 경우")
    @Test
    void name3() {
        Map<Location, Piece> route = new HashMap<>();
        Pawn givenPiece = new Pawn(Team.BLACK);
        Pawn counterPiece = new Pawn(Team.WHITE);
        // Pawn destinaionPiece = new Pawn(Team.WHITE);

        Location now = new Location(7, 'a');
        Location after = new Location(5, 'a');

        route.put(new Location(6, 'a'), counterPiece);

        Route route1 = new Route(route, now, after);

        boolean actual = givenPiece.canMove(route1);
        assertThat(actual).isFalse();
    }

    @DisplayName("폰의 직선 위치에 적이 있는 경우")
    @Test
    void name4() {
        Map<Location, Piece> board = new HashMap<>();

        Pawn givenPiece = new Pawn(Team.BLACK);
        Location now = new Location(7, 'a');
        board.put(now, givenPiece);

        Pawn counterPiece = new Pawn(Team.WHITE);
        Location after = new Location(6, 'a');
        board.put(after, counterPiece);

        Route route = new Route(board, now, after);

        boolean actual = givenPiece.canMove(route);
        assertThat(actual).isFalse();
    }

    @DisplayName("폰의 직선 위치에 적이 있는 경우")
    @Test
    void name5() {
        Map<Location, Piece> board = new HashMap<>();

        Pawn givenPiece = new Pawn(Team.BLACK);
        Pawn destinaionPiece = new Pawn(Team.WHITE);
        Location now = new Location(7, 'a');
        Location after = new Location(6, 'a');
        board.put(now, givenPiece);
        board.put(after, destinaionPiece);

        Route route = new Route(board, now, after);
        boolean actual = givenPiece.canMove(route);
        assertThat(actual).isFalse();
    }
}