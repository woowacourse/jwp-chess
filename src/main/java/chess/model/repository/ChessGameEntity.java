package chess.model.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("CHESS_GAME_TB")
public class ChessGameEntity {

    @Id
    private Integer id;
    private Integer roomId;
    @Column("TURN_NM")
    private String turnName;
    @Column("PROCEEDING_YN")
    private String isProceeding;
    @Column("BLACK_USER_NM")
    private String blackName;
    @Column("WHITE_USER_NM")
    private String whiteName;
    private Double blackScore;
    private Double whiteScore;

    public ChessGameEntity(Integer roomId, String turnName, String isProceeding,
        String blackName, String whiteName, Double blackScore, Double whiteScore) {
        this.roomId = roomId;
        this.turnName = turnName;
        this.isProceeding = isProceeding;
        this.blackName = blackName;
        this.whiteName = whiteName;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getTurnName() {
        return turnName;
    }

    public String getIsProceeding() {
        return isProceeding;
    }

    public String getBlackName() {
        return blackName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public Double getBlackScore() {
        return blackScore;
    }

    public Double getWhiteScore() {
        return whiteScore;
    }
}
