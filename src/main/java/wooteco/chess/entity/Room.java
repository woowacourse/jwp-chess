package wooteco.chess.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.piece.GamePieces;

@Table("room")
public class Room {

    private static final int MAXIMUM_LENGTH = 10;

    @Id
    private Long id;
    private Integer turn;
    private String name;
    private Set<Cell> cells = new HashSet<>();

    public Room() {
    }

    public Room(String name) {
        validateName(name);
        this.name = name;
    }

    public Room(String name, Integer turn) {
        this.name = name;
        this.turn = turn;
    }

    private void validateName(String name) {
        if (name.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException("이름은 최대 10자입니다.");
        }
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

    public void updateTurn(Integer turn) {
        this.turn = turn;
    }

    public Board createBoard() {
        // Map<Position, GamePiece> boardMap = new HashMap<>();
        //
        // for (Cell cell : cells) {
        //     Position position = Position.from(cell.getPosition());
        //     GamePiece gamePiece = GamePieces.from(cell.getPiece());
        //     boardMap.put(position, gamePiece);
        // }

        Map<Position, GamePiece> boardMap = cells.stream()
                .collect(Collectors.toMap(cell -> Position.from(cell.getPosition()), cell -> GamePieces.from(cell.getPiece())));

        return BoardFactory.of(boardMap);
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Integer getTurn() {
        return turn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Room room = (Room)o;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}