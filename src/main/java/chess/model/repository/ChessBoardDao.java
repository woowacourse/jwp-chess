package chess.model.repository;

import static chess.model.repository.template.JdbcTemplate.makeQuery;

import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.EnPassant;
import chess.model.domain.board.Square;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.PieceFactory;
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

    public void create(Integer gameId, Map<Square, Piece> chessBoard,
        Map<Square, Boolean> castlingElements, Map<Square, Square> enPassant) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "INSERT INTO CHESS_BOARD_TB(GAME_ID, BOARDSQUARE_NM, PIECE_NM, CASTLING_ELEMENT_YN, EN_PASSANT_NM)"
            , "VALUES (?, ?, ?, ?, ?)"
        );
        PreparedStatementSetter pss = pstmt -> {
            for (Square square : chessBoard.keySet()) {
                pstmt.setInt(1, gameId);
                pstmt.setString(2, square.getName());
                pstmt.setString(3, PieceFactory.getName(chessBoard.get(square)));
                pstmt.setString(4, JdbcTemplate.convertYN(castlingElements.get(square)));
                pstmt.setObject(5, enPassant.keySet().stream()
                    .filter(key -> enPassant.containsKey(square))
                    .map(enSquare -> enPassant.get(square).getName())
                    .findFirst()
                    .orElse(null));
                pstmt.addBatch();
                pstmt.clearParameters();
            }
        };
        jdbcTemplate.executeUpdateWhenLoop(query, pss);
    }

    public Set<CastlingSetting> findCastlingElements(Integer gameId) {
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
                        PieceFactory.getPiece(rs.getString("PIECE_NM"))));
            }
            return castlingElements;
        };
        return jdbcTemplate.executeQuery(query, pss, mapper);
    }

    public EnPassant findEnpassantBoard(Integer gameId) {
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

    public Map<Square, Piece> findBoard(Integer gameId) {
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
                    PieceFactory.getPiece(rs.getString("PIECE_NM")));
            }
            return board;
        };
        return jdbcTemplate.executeQuery(query, pss, mapper);
    }

    public void delete(Integer gameId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "DELETE FROM CHESS_BOARD_TB",
            " WHERE GAME_ID = ?"
        );
        PreparedStatementSetter pss = pstmt -> pstmt.setInt(1, gameId);
        jdbcTemplate.executeUpdate(query, pss);
    }
}
