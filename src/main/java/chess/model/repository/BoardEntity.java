package chess.model.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("CHESS_BOARD_TB")
public class BoardEntity {

    @Id
    private Integer id;
    @Column("GAME_ID")
    private Integer gameId;
    @Column("BOARDSQUARE_NM")
    private String squareName;
    @Column("PIECE_NM")
    private String PieceName;
    @Column("CASTLING_ELEMENT_YN")
    private String castlingElementYN;
    @Column("EN_PASSANT_NM")
    private String enPassantName;

    public BoardEntity(Integer gameId, String squareName, String pieceName,
        String castlingElementYN, String enPassantName) {
        this.gameId = gameId;
        this.squareName = squareName;
        PieceName = pieceName;
        this.castlingElementYN = castlingElementYN;
        this.enPassantName = enPassantName;
    }

    public Integer getGameId() {
        return gameId;
    }

    public String getSquareName() {
        return squareName;
    }

    public String getPieceName() {
        return PieceName;
    }

    public String getCastlingElementYN() {
        return castlingElementYN;
    }

    public String getEnPassantName() {
        return enPassantName;
    }

    @Override
    public String toString() {
        return "BoardEntity{" +
            "gameId=" + gameId +
            ", squareName='" + squareName + '\'' +
            ", PieceName='" + PieceName + '\'' +
            ", castlingElementYN='" + castlingElementYN + '\'' +
            ", enPassantName='" + enPassantName + '\'' +
            '}';
    }
}
