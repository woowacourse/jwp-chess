package chess.domain;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.exception.InvalidMovementException;

import java.util.List;
import java.util.Map;

public class Board {
    private final Map<Position, Piece> board;

    public Board(Map<Position, Piece> board) {
        this.board = board;
    }

    public static Board getGamingBoard() {
        return new Board(BoardInitializer.init());
    }

    public void move(Position from, Position to, Side playerSide) {
        Piece piece = board.get(from);

        // 내 기물인지
        if (!piece.isSideEqualTo(playerSide)) {
            throw new InvalidMovementException("자신의 기물만 움직일 수 있습니다.");
        }

        // 이동 경로 구하기
        checkRoute(piece.route(from, to));

        // to에 내 기물 있는지
        if (board.get(to).isSideEqualTo(playerSide)) {
            throw new InvalidMovementException("이동하려는 위치에 자신의 기물이 존재합니다.");
        }

        // 폰: 대각선이면 상대 기물있어야 함, 직선이면 블랭크여야 함
        if (piece.isPawn()) {
            if (Math.abs(Position.differenceOfRow(from, to)) == Math.abs(Position.differenceOfColumn(from, to))) {
                if (board.get(to).isBlank()) {
                    throw new InvalidMovementException("이동하려는 위치에 상대 기물이 존재하지 않습니.");
                }
            }
            if (Position.differenceOfColumn(from, to) == 0 || Position.differenceOfRow(from, to) == 0) {
                if (!board.get(to).isBlank()) {
                    throw new InvalidMovementException("이동하려는 위치에 기물이 존재합니다.");
                }
            }
        }

        board.put(to, piece);
        board.put(from, new Blank()); // todo Blank 캐싱
        piece.moved();
    }

    private void checkRoute(List<Position> route) {
        route.stream()
                .map(board::get)
                .map(Piece::isBlank)
                .filter(isBlank -> !isBlank)
                .findAny()
                .ifPresent(isBlank -> {
                    throw new InvalidMovementException("이동경로에 다른 기물이 존재합니다.");
                });
    }

    public String getInitial(Position position) {
        return board.get(position).getInitial();
    }
}
