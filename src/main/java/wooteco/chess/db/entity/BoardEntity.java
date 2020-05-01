package wooteco.chess.db.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("piece")
public class BoardEntity {
    @Id
    @Column("piece_id")
    private Long id;
    private Long roomId;
    private String pieceName;
    private String pieceTeam;
    private String piecePosition;

    public BoardEntity(String pieceName, String pieceTeam, String piecePosition) {
        this.pieceName = pieceName;
        this.pieceTeam = pieceTeam;
        this.piecePosition = piecePosition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getPieceName() {
        return pieceName;
    }

    public void setPieceName(String pieceName) {
        this.pieceName = pieceName;
    }

    public String getPieceTeam() {
        return pieceTeam;
    }

    public void setPieceTeam(String pieceTeam) {
        this.pieceTeam = pieceTeam;
    }

    public String getPiecePosition() {
        return piecePosition;
    }

    public void setPiecePosition(String piecePosition) {
        this.piecePosition = piecePosition;
    }
}
