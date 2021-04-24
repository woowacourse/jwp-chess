package chess.dao;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.*;
import chess.exception.NoSavedGameException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveGame(SavedGameDto savedGameDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertGameQuery = "insert into game(turn) values(?)";
        this.jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(
                    insertGameQuery,
                    new String[]{"game_id"});
            pstmt.setString(1, savedGameDto.getCurrentTurnColor());
            return pstmt;
        }, keyHolder);
        int gameId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        savePiecesByGameId(gameId, savedGameDto.getChessBoardDto().board());
        return gameId;
    }

    private void savePiecesByGameId(int gameId, Map<String, PieceDto> board) {
        for (String position : board.keySet()) {
            String query = "insert into piece(game_id, name, color, position) values(?, ?, ?, ?)";
            PieceDto piece = board.get(position);
            this.jdbcTemplate.update(query, gameId, piece.getName(), piece.getColor(), position);
        }
    }

    public SavedGameDto loadGame(int gameId) {
        String gameQuery = "SELECT turn FROM game WHERE game_id = ?";
        String currentTurn = this.jdbcTemplate.queryForObject(gameQuery, String.class, gameId);

        if (currentTurn != null) {
            String queryPiece = "SELECT * FROM piece WHERE game_id = ?";
            ChessBoard chessBoard = this.jdbcTemplate.queryForObject(queryPiece, chessBoardRowMapper, gameId);
            return new SavedGameDto(ChessBoardDto.from(chessBoard), currentTurn);
        }
        throw new NoSavedGameException("저장된 게임이 없습니다.");
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

    public void updatePiecesByGameId(ChessBoardDto chessBoardDto, int gameId) {
        deletePiecesByGameId(gameId);
        savePiecesByGameId(gameId, chessBoardDto.board());
    }

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

    public List<Integer> loadGameList() {
        String query = "SELECT game_id FROM game ";
        return this.jdbcTemplate.query(query, (resultSet, rowNum) -> resultSet.getInt("game_id"));
    }

    public int findRoomCount(String roomName) {
        String query = "SELECT count(*) FROM room WHERE name = ?";
        return this.jdbcTemplate.queryForObject(query, Integer.class, roomName);
    }
}
