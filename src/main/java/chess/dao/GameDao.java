package chess.dao;

import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.ChessBoardDto;
import chess.dto.PieceDeserializeTable;
import chess.dto.PieceDto;
import chess.exception.NoSavedGameException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GameDao {
    private JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveNewGame(ChessGameManager chessGameManager) {
        Color currentTurnColor = chessGameManager.getCurrentTurnColor();

        String insertGameQuery = "insert into game(turn) values(?)";
        this.jdbcTemplate.update(insertGameQuery, currentTurnColor.name());

        String findGameIdQuery = "select last_insert_id()";
        int gameId = this.jdbcTemplate.queryForObject(findGameIdQuery, Integer.class);

        savePiecesByGameId(chessGameManager, gameId);
        return gameId;
    }

    private final RowMapper<ChessBoard> chessBoardRowMapper = (resultSet, rowNum) -> {
        Map<Position, Piece> board = new HashMap<>();

        do {
            Piece piece = PieceDeserializeTable.deserializeFrom(
                    resultSet.getString("name"),
                    Color.of(resultSet.getString("color"))
            );
            Position position = Position.of(resultSet.getString("position"));
            board.put(position, piece);
        } while (resultSet.next());
        return ChessBoard.from(board);
    };

    private final RowMapper<Color> colorRowMapper = (resultSet, rowNum) -> {
        return Color.of(resultSet.getString("turn"));
    };

    public ChessGameManager loadGame(int gameId) {
        String gameQuery = "SELECT turn FROM game WHERE game_id = ?";
        Color currentTurn = this.jdbcTemplate.queryForObject(gameQuery, colorRowMapper, gameId);

        if (currentTurn != null) {
            String queryPiece = "SELECT * FROM piece WHERE game_id = ?";
            ChessBoard chessBoard = this.jdbcTemplate.queryForObject(queryPiece, chessBoardRowMapper, gameId);

            ChessGameManager chessGameManager = new ChessGameManager();
            chessGameManager.load(chessBoard, currentTurn);
            return chessGameManager;
        }
        throw new NoSavedGameException("저장된 게임이 없습니다.");
    }

    public void updatePiecesByGameId(ChessGameManager chessGameManager, int gameId) {
        String deletePiecesQuery = "DELETE FROM piece WHERE game_id = ?";
        this.jdbcTemplate.update(deletePiecesQuery, gameId);

        savePiecesByGameId(chessGameManager, gameId);
    }

    private void savePiecesByGameId(ChessGameManager chessGameManager, int gameId) {
        Map<String, PieceDto> board = ChessBoardDto.from(chessGameManager.getBoard()).board();
        for (String position : board.keySet()) {
            String query = "insert into piece(game_id, name, color, position) values(?, ?, ?, ?)";

            PieceDto piece = board.get(position);
            this.jdbcTemplate.update(query, gameId, piece.getName(), piece.getColor(), position);
        }
    }

    public void updateTurnByGameId(ChessGameManager chessGameManager, int gameId) {
        Color currentTurnColor = chessGameManager.getCurrentTurnColor();
        String query = "UPDATE game set turn=? WHERE game_id = ?";
        this.jdbcTemplate.update(query, currentTurnColor.name(), gameId);
    }

    public List<Integer> loadGames() {
        String query = "SELECT game_id FROM game ";
        return this.jdbcTemplate.query(query, (resultSet, rowNum) -> resultSet.getInt("game_id"));
    }
}
