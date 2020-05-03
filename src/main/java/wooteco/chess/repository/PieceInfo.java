package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("piece_info")
public class PieceInfo {

    @Id
    private Long id;
    private String piece;
    private String position;
    private String roomNameHash;

    public PieceInfo() {
    }

    public PieceInfo(String piece, String position, String roomNameHash) {
        this.piece = piece;
        this.position = position;
        this.roomNameHash = roomNameHash;
    }

    public PieceInfo(Long id, String piece, String position, String roomNameHash) {
        this.id = id;
        this.piece = piece;
        this.position = position;
        this.roomNameHash = roomNameHash;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public String getPiece() {
        return piece;
    }

    public String getPosition() {
        return position;
    }

    public String getRoomNameHash() {
        return roomNameHash;
    }
}
