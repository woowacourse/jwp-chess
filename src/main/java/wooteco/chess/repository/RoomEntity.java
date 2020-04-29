package wooteco.chess.repository;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.piece.Team;

@Table("room")
public class RoomEntity {
    @Id
    private Long id;
    private String name;
    private Team turn;
    private Set<PieceEntity> pieceEntities = new HashSet<>();

    public RoomEntity() {
    }

    public RoomEntity(String name, Team turn) {
        this.name = name;
        this.turn = turn;
    }

    public RoomEntity(Long id, String name, Team turn, Set<PieceEntity> pieceEntities) {
        this.id = id;
        this.name = name;
        this.turn = turn;
        this.pieceEntities = pieceEntities;
    }

    public RoomEntity(String name, Team turn, Set<PieceEntity> pieceEntities) {
        this.name = name;
        this.turn = turn;
        this.pieceEntities = pieceEntities;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Team getTurn() {
        return turn;
    }

    public Set<PieceEntity> getPieceEntities() {
        return pieceEntities;
    }
}