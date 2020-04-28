package spring.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("chessgame2")
public class ChessGameEntity {
    @Id
    Long id;
    String whiteName;
    String blackName;
    Boolean isTurnBlack;
    Set<PieceEntity> pieces;

    public ChessGameEntity(String whiteName, String blackName, Boolean isTurnBlack, Set<PieceEntity> pieces) {
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.isTurnBlack = isTurnBlack;
        this.pieces = pieces;
    }

    public void add(PieceEntity pieceEntity) {
        pieces.add(pieceEntity);
    }

    public void delete(PieceEntity pieceEntity) {
        pieces.remove(pieceEntity);
    }

    public void update(Set<PieceEntity> pieces) {
        this.pieces = pieces;
    }
}
