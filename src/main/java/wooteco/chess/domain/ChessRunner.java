package wooteco.chess.domain;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;
import wooteco.chess.domain.score.Score;

public class ChessRunner {
    private final Board board;
    private Team currentTeam;

    public ChessRunner() {
        this.board = new Board();
        this.currentTeam = Team.WHITE;
    }

    public void updateBoard(String source, String target) {
        Position sourcePosition = Positions.of(source);
        Position targetPosition = Positions.of(target);
        Piece selectedPiece = this.board.getPiece(sourcePosition);

        validateMovement(sourcePosition, targetPosition, selectedPiece);
        this.board.updateBoard(sourcePosition, targetPosition);
        this.currentTeam = currentTeam.changeTeam();
    }

    private void validateMovement(Position sourcePosition, Position targetPosition, Piece selectedPiece) {
        if (selectedPiece.isEnemy(currentTeam)) {
            throw new IllegalArgumentException("현재 차례가 아닙니다.");
        }

        if (!(selectedPiece.movable(sourcePosition, targetPosition, board))) {
            throw new IllegalArgumentException("이동할 수 없는 곳입니다.");
        }
    }

    public void clearBoard() {
        board.clear();
    }

    public Team findWinner() {
        return this.board.checkWinner();
    }

    public double calculateScore() {
        return Score.calculateScore(board.get(), currentTeam);
    }

    public Board getBoard() {
        return this.board;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }
}
