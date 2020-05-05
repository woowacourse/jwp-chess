package wooteco.chess.db;

import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

public class Room {
    @Id
    private Long id;
    private String name;
    private Set<ChessPiece> chessPieces = new HashSet<>();
    private Set<MoveHistory> moveHistories = new HashSet<>();

    Room() {
    }

    public Room(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<ChessPiece> getChessPieces() {
        return chessPieces;
    }

    public void addChessPieces(Set<ChessPiece> chessPieces) {
        this.chessPieces.addAll(chessPieces);
    }

    public Set<MoveHistory> getMoveHistories() {
        return moveHistories;
    }

    public void addMoveHistory(MoveHistory moveHistory) {
        this.moveHistories.add(moveHistory);
    }
}
