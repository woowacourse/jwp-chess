package chess.dao;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.ChessBoardDto;
import chess.dto.PieceDeserializeTable;
import chess.dto.PieceDto;
import chess.dto.SavedGameDto;
import chess.exception.NoSavedGameException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveGame(SavedGameDto savedGameDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertGameQuery = "INSERT INTO game(turn) VALUES(?)";
        this.jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(
                    insertGameQuery,
                    new String[]{"game_id"});
            pstmt.setString(1, savedGameDto.getCurrentTurnColor());
            return pstmt;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public SavedGameDto loadGame(int gameId) {
        String gameQuery = "SELECT turn FROM game WHERE game_id = ?";
        String currentTurn = this.jdbcTemplate.queryForObject(gameQuery, String.class, gameId);

        if (currentTurn == null) {
            throw new NoSavedGameException("저장된 게임이 없습니다.");
        }

        String queryPiece = "SELECT * FROM piece WHERE game_id = ?";
        ChessBoard chessBoard = this.jdbcTemplate.queryForObject(queryPiece, chessBoardRowMapper, gameId);
        return new SavedGameDto(ChessBoardDto.from(chessBoard), currentTurn);
    }

    private final RowMapper<ChessBoard> chessBoardRowMapper = (resultSet, rowNum) -> {
        Map<Position, Piece> board = new HashMap<>();

        do {
            Piece piece = PieceDeserializeTable.deserializeFrom(
                    resultSet.getString("name"),
                    Color.of(resultSet.getString("color")));
            Position position = Position.of(resultSet.getString("position"));
            board.put(position, piece);
        } while (resultSet.next());
        return ChessBoard.from(board);
    };

    public void updateTurnByGameId(Color currentTurnColor, int gameId) {
        String query = "UPDATE game set turn=? WHERE game_id = ?";
        this.jdbcTemplate.update(query, currentTurnColor.name(), gameId);
    }

    public void deleteGameByGameId(int gameId) {
        this.jdbcTemplate.update("DELETE FROM game WHERE game_id = ?", gameId);
    }

    public void deletePiecesByGameId(int gameId) {
        this.jdbcTemplate.update("DELETE FROM piece WHERE game_id = ?", gameId);
    }

    public void deletePieceByGameId(int gameId, String endPosition) {
        this.jdbcTemplate.update("DELETE FROM piece WHERE game_id = ? AND position = ?", gameId, endPosition);
    }

    public void updatePiecePositionByGameId(int gameId, String startPosition, String endPosition) {
        String query = "UPDATE piece SET position=? WHERE game_id=? AND position=?";
        this.jdbcTemplate.update(query, endPosition, gameId, startPosition);
    }

    public void savePiecesByGameId(int gameId, Map<String, PieceDto> chessBoard) {
        String query = "INSERT INTO piece(game_id, name, color, position) VALUES(?, ?, ?, ?)";
        this.jdbcTemplate.batchUpdate(query, chessBoard.keySet(), chessBoard.size(), (ps, position) -> {
            ps.setInt(1, gameId);
            PieceDto pieceDto = chessBoard.get(position);
            ps.setString(2, pieceDto.getName());
            ps.setString(3, pieceDto.getColor());
            ps.setString(4, position);
        });
    }
}
