package wooteco.chess.entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.piece.GamePieces;

@Table("game")
public class GameEntity {

    private Integer turn;

    @Column("game")
    private Set<Cell> cells = new HashSet<>();

    public GameEntity() {
    }

    public GameEntity(int turn) {
        this.turn = turn;
    }

    public GameEntity(int turn, Set<Cell> cells) {
        this.turn = turn;
        this.cells = cells;
    }

    public void addBoard(Board board) {
        for (Map.Entry<Position, GamePiece> entry : board.getBoard().entrySet()) {
            cells.add(new Cell(entry.getKey().getName(), entry.getValue().getName()));
        }
    }

    public void updateBoard(Board board) {
        cells.clear();
        addBoard(board);
    }

    public Board createBoard() {
        Map<Position, GamePiece> boardMap = cells.stream()
                .collect(Collectors.toMap(cell -> Position.from(cell.getPosition()), cell -> GamePieces.from(cell.getPiece())));

        return BoardFactory.of(boardMap);
    }

    public void updateTurn(Integer turn) {
        this.turn = turn;
    }

    public Integer getTurn() {
        return turn;
    }

    public Set<Cell> getCells() {
        return cells;
    }
}
