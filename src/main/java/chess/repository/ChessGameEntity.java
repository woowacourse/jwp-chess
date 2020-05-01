package chess.repository;

import chess.domain.board.ChessGame;
import chess.domain.board.TeamScore;
import chess.domain.piece.Team;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("CHESS_GAME_TB")
public class ChessGameEntity {

    @Id
    private Integer id;
    @Column("TURN_NM")
    private String turnName;
    @Column("PROCEEDING_YN")
    private String proceeding;
    @Column("BLACK_USER_NM")
    private String blackName;
    @Column("WHITE_USER_NM")
    private String whiteName;
    private Double blackScore;
    private Double whiteScore;
    private Set<BoardEntity> boardEntities = new HashSet<>();

    public ChessGameEntity(String turnName, String proceeding,
        String blackName, String whiteName, Double blackScore, Double whiteScore) {
        this.turnName = turnName;
        this.proceeding = proceeding;
        this.blackName = blackName;
        this.whiteName = whiteName;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }

    public void addBoard(BoardEntity boardEntity) {
        this.boardEntities.add(boardEntity);
    }

    public boolean isProceeding() {
        return this.proceeding.equals("Y");
    }

    public void update(ChessGame chessGame, String proceed) {
        this.turnName = chessGame.getTurn().getName();
        this.proceeding = proceed;
        this.blackScore = chessGame.deriveTeamScore().get(Team.BLACK);
        this.whiteScore = chessGame.deriveTeamScore().get(Team.WHITE);
    }

    public void clearBoard() {
        this.boardEntities.clear();
    }

    public Integer getId() {
        return id;
    }

    public String getTurnName() {
        return turnName;
    }

    public String getProceeding() {
        return proceeding;
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

    public void setProceeding(String proceeding) {
        this.proceeding = proceeding;
    }

    public TeamScore makeTeamScore() {
        Map<Team, Double> teamScore = new HashMap<>();
        teamScore.put(Team.BLACK, blackScore);
        teamScore.put(Team.WHITE, whiteScore);
        return new TeamScore(teamScore);
    }

    public Map<Team, String> makeUserNames() {
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, blackName);
        userNames.put(Team.WHITE, whiteName);
        return userNames;
    }
}
