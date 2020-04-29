package wooteco.chess.boot.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("piece")
public class PieceEntity {
    @Id
    private Long id;
    private Long roomId;
    private String place;
    private String piece;

    public PieceEntity(Long roomId, String place, String piece) {
        this.roomId = roomId;
        this.place = place;
        this.piece = piece;
    }

    private Long getId() {
        return id;
    }

    private Long getRoomId() {
        return roomId;
    }

    private String getPlace() {
        return place;
    }

    private String getPiece() {
        return piece;
    }
}
