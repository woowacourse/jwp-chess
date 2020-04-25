package chess.model.repository;

import static chess.model.repository.template.JdbcTemplate.getPssFromParams;
import static chess.model.repository.template.JdbcTemplate.makeQuery;

import chess.model.domain.board.Square;
import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.EnPassant;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.PieceFactory;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveOrder;
import chess.model.repository.template.JdbcTemplate;
import chess.model.repository.template.PreparedStatementSetter;
import chess.model.repository.template.ResultSetMapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChessBoardDao {

    private final static ChessBoardDao INSTANCE = new ChessBoardDao();

    private ChessBoardDao() {
    }

    public static ChessBoardDao getInstance() {
        return INSTANCE;
    }

    public void insert(int gameId, Map<Square, Piece> board,
        Map<Square, Boolean> castlingElements) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "INSERT INTO CHESS_BOARD_TB(GAME_ID, BOARDSQUARE_NM, PIECE_NM, CASTLING_ELEMENT_YN)"
            , "VALUES (?, ?, ?, ?)"
        );
        PreparedStatementSetter pss = pstmt -> {
            for (Square boardSquare : board.keySet()) {
                pstmt.setInt(1, gameId);
                pstmt.setString(2, boardSquare.getName());
                pstmt.setString(3, PieceFactory.getName(board.get(boardSquare)));
                pstmt.setString(4, changeBooleanToString(castlingElements.get(boardSquare)));
                pstmt.addBatch();
                pstmt.clearParameters();
            }
        };
        jdbcTemplate.executeUpdateWhenLoop(query, pss);
    }

    private String changeBooleanToString(boolean changer) {
        return changer ? "Y" : "N";
    }

    public void insertBoard(int gameId, Square square, Piece piece) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "INSERT INTO CHESS_BOARD_TB(GAME_ID, BOARDSQUARE_NM, PIECE_NM, CASTLING_ELEMENT_YN)",
            "VALUES (?, ?, ?, 'N')"
        );
        PreparedStatementSetter pss = JdbcTemplate
            .getPssFromParams(gameId, square.getName(), PieceFactory.getName(piece));
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void deleteBoardSquare(int gameId, Square square) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "DELETE FROM CHESS_BOARD_TB",
            " WHERE GAME_ID = ?",
            "   AND BOARDSQUARE_NM = ?"
        );
        PreparedStatementSetter pss = JdbcTemplate.getPssFromParams(gameId, square.getName());
        jdbcTemplate.executeUpdate(query, pss);
    }

    public Set<CastlingSetting> getCastlingElements(int gameId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "SELECT BOARDSQUARE_NM",
            "     , PIECE_NM",
            "  FROM CHESS_BOARD_TB",
            " WHERE GAME_ID = ?",
            "   AND CASTLING_ELEMENT_YN = 'Y'"
        );
        PreparedStatementSetter pss = pstmt -> pstmt.setInt(1, gameId);
        ResultSetMapper<Set<CastlingSetting>> mapper = rs -> {
            Set<CastlingSetting> castlingElements = new HashSet<>();
            while (rs.next()) {
                castlingElements
                    .add(CastlingSetting.of(Square.of(rs.getString("BOARDSQUARE_NM")),
                        PieceFactory.of(rs.getString("PIECE_NM"))));
            }
            return castlingElements;
        };
        return jdbcTemplate.executeQuery(query, pss, mapper);
    }

    public EnPassant getEnpassantBoard(int gameId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "SELECT EN_PASSANT_NM",
            "     , BOARDSQUARE_NM",
            "  FROM CHESS_BOARD_TB",
            " WHERE GAME_ID = ?",
            "   AND EN_PASSANT_NM IS NOT NULL"
        );
        PreparedStatementSetter pss = pstmt -> pstmt.setInt(1, gameId);
        ResultSetMapper<EnPassant> mapper = rs -> {
            Map<Square, Square> board = new HashMap<>();
            while (rs.next()) {
                board.put(Square.of(rs.getString("EN_PASSANT_NM")),
                    Square.of(rs.getString("BOARDSQUARE_NM")));
            }
            return new EnPassant(board);
        };
        return jdbcTemplate.executeQuery(query, pss, mapper);
    }

    public Map<Square, Piece> getBoard(int gameId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "SELECT BOARDSQUARE_NM",
            "     , PIECE_NM",
            "  FROM CHESS_BOARD_TB",
            " WHERE GAME_ID = ?"
        );
        PreparedStatementSetter pss = pstmt -> pstmt.setInt(1, gameId);
        ResultSetMapper<Map<Square, Piece>> mapper = rs -> {
            Map<Square, Piece> board = new HashMap<>();
            while (rs.next()) {
                board.put(Square.of(rs.getString("BOARDSQUARE_NM")),
                    PieceFactory.of(rs.getString("PIECE_NM")));
            }
            return board;
        };
        return jdbcTemplate.executeQuery(query, pss, mapper);
    }

    public void delete(int gameId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "DELETE FROM CHESS_BOARD_TB",
            " WHERE GAME_ID = ?"
        );
        PreparedStatementSetter pss = pstmt -> pstmt.setInt(1, gameId);
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void copyBoardSquare(int gameId, MoveInfo moveInfo) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "INSERT INTO CHESS_BOARD_TB(GAME_ID, BOARDSQUARE_NM, PIECE_NM, CASTLING_ELEMENT_YN)",
            "VALUES (?, ?, (",
            "       SELECT PIECE_NM",
            "         FROM CHESS_BOARD_TB AS BOARD",
            "         JOIN CHESS_GAME_TB  AS GAME",
            "        WHERE BOARD.GAME_ID = GAME.ID",
            "          AND GAME.PROCEEDING_YN = 'Y'",
            "          AND GAME.ID = ? ",
            "          AND BOARDSQUARE_NM = ?), 'N')"
        );
        PreparedStatementSetter pss = JdbcTemplate
            .getPssFromParams(gameId, moveInfo.get(MoveOrder.TO).getName(), gameId,
                moveInfo.get(MoveOrder.FROM).getName());
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void updatePromotion(int gameId, Square finishPawnBoard, Piece hopePiece) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "UPDATE CHESS_BOARD_TB",
            "   SET PIECE_NM = ?",
            " WHERE GAME_ID = ?",
            "   AND BOARDSQUARE_NM = ?"
        );
        PreparedStatementSetter pss = JdbcTemplate
            .getPssFromParams(PieceFactory.getName(hopePiece), gameId, finishPawnBoard.getName());
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void deleteEnpassant(int gameId, Square enpassantSquare) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "DELETE FROM CHESS_BOARD_TB",
            " WHERE GAME_ID = ?",
            "   AND EN_PASSANT_NM = ?"
        );
        PreparedStatementSetter pss = JdbcTemplate
            .getPssFromParams(gameId, enpassantSquare.getName());
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void updateEnPassant(int gameId, MoveInfo moveInfo) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "UPDATE CHESS_BOARD_TB",
            "   SET EN_PASSANT_NM = ?",
            " WHERE GAME_ID = ?",
            "   AND BOARDSQUARE_NM = ?"
        );
        PreparedStatementSetter pss = JdbcTemplate
            .getPssFromParams(EnPassant.getBetween(moveInfo).getName(), gameId,
                moveInfo.get(MoveOrder.TO).getName());
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void deleteMyEnpassant(int gameId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "UPDATE CHESS_BOARD_TB",
            "   SET EN_PASSANT_NM = NULL",
            " WHERE GAME_ID = ?",
            "   AND PIECE_NM = (SELECT CONCAT(TURN_NM, '_PAWN')",
            "                     FROM CHESS_GAME_TB",
            "                    WHERE ID = ?)"
        );
        PreparedStatementSetter pss = getPssFromParams(gameId, gameId);
        jdbcTemplate.executeUpdate(query, pss);
    }
}
