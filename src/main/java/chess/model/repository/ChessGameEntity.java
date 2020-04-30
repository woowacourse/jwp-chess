package chess.model.repository;

import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.ChessGame;
import chess.model.domain.board.Square;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.PieceFactory;
import chess.model.domain.piece.Team;
import chess.util.TFAndYNConverter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
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

    protected ChessGameEntity() {
    }

    public ChessGameEntity(String turnName, String proceeding,
        String blackName, String whiteName, Double blackScore, Double whiteScore) {
        this.turnName = turnName;
        this.proceeding = proceeding;
        this.blackName = blackName;
        this.whiteName = whiteName;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
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

    public void saveBoard(ChessGame chessGame) {
        boardEntities.clear();
        Map<Square, Square> enPassants = makeEnPassants(chessGame);
        Map<Square, Boolean> castlingElements = makeCastlingElements(chessGame.getChessBoard(),
            chessGame.getCastlingElements());
        Map<Square, Piece> chessBoard = chessGame.getChessBoard();

        for (Square square : chessBoard.keySet()) {
            boardEntities.add(new BoardEntity(
                square.getName(),
                PieceFactory.getName(chessBoard.get(square)),
                TFAndYNConverter.convertYN(castlingElements.get(square)),
                enPassants.keySet().stream()
                    .filter(key -> enPassants.containsKey(square))
                    .map(enSquare -> enPassants.get(square).getName())
                    .findFirst()
                    .orElse(null)));
        }
    }

    private Map<Square, Square> makeEnPassants(ChessGame chessGame) {
        return chessGame.getEnPassants().entrySet().stream()
            .collect(Collectors.toMap(Entry::getValue, Entry::getKey));
    }

    private Map<Square, Boolean> makeCastlingElements(Map<Square, Piece> chessBoard,
        Set<CastlingSetting> castlingElements) {
        return chessBoard.keySet().stream()
            .collect(Collectors.toMap(square -> square,
                square -> makeCastlingElements(square, chessBoard.get(square), castlingElements)));
    }

    private boolean makeCastlingElements(Square square, Piece piece,
        Set<CastlingSetting> castlingElements) {
        return castlingElements.stream()
            .anyMatch(castlingSetting -> castlingSetting.isCastlingBefore(square, piece));
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

    public void setProceeding(String proceeding) {
        this.proceeding = proceeding;
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

    public Set<BoardEntity> getBoardEntities() {
        return boardEntities;
    }
}
